/*
 初始化视图和过程
 */
-- ----------------------------
-- View structure for v_all_rate
-- ----------------------------
DROP VIEW IF EXISTS `v_all_rate`;
CREATE ALGORITHM=UNDEFINED DEFINER=`ec`@`%` SQL SECURITY DEFINER VIEW `v_all_rate` AS select `t_rate_history`.`referrer_id` AS `referrer_id`,`t_rate_history`.`company_info_id` AS `company_info_id`,`t_rate_history`.`date` AS `date`,`t_rate_history`.`company_rate` AS `company_rate` from `t_rate_history` union select `t_referrer_company`.`referrer_id` AS `referrer_id`,`t_referrer_company`.`company_info_id` AS `company_info_id`,now() AS `date`,`t_referrer_company`.`company_rate` AS `company_rate` from `t_referrer_company` ;

-- ----------------------------
-- Procedure structure for activityDisplayPro
-- ----------------------------
DROP PROCEDURE IF EXISTS `activityDisplayPro`;
DELIMITER ;;
CREATE DEFINER=`ec`@`%` PROCEDURE `activityDisplayPro`()
BEGIN
    UPDATE t_activity a
SET a.display = 1
WHERE
    a.activity_type = 1
AND a.activity_status = 2
AND DATE_FORMAT(a.end_time, '%Y-%m-%d') = date_sub(curdate(), INTERVAL 1 DAY);


END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for activityPro
-- ----------------------------
DROP PROCEDURE IF EXISTS `activityPro`;
DELIMITER ;;
CREATE DEFINER=`ec`@`%` PROCEDURE `activityPro`()
BEGIN
UPDATE t_activity a
RIGHT JOIN (
    SELECT
        s.activity_id,
        COUNT(*)
    FROM
        t_activity_sku s
    GROUP BY
        s.activity_id
) m ON a.id = m.activity_id
SET a.activity_status = (
    CASE
    WHEN a.start_time = NOW()
    AND a.activity_status = 0 THEN
        1
    WHEN a.end_time = NOW()
    AND a.activity_type = 1
    AND a.activity_status = 1 THEN
        2
    ELSE
        a.activity_status
    END
);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for orderPro
-- ----------------------------
DROP PROCEDURE IF EXISTS `orderPro`;
DELIMITER ;;
CREATE DEFINER=`ec`@`%` PROCEDURE `orderPro`()
BEGIN
UPDATE t_order r
SET r.`status` = 7
WHERE
    r.`no` IN (
        SELECT
            t.`no`
        FROM
            (
                SELECT
                    o.`no`
                FROM
                    t_order o
                INNER JOIN t_order_sku os ON o.id = os.order_id
                INNER JOIN t_order_sku_activity osa ON o.id = osa.order_id
                AND os.sku_id = osa.activity_skuId
                RIGHT JOIN t_activity_sku tas ON osa.activity_skuId = tas.sku_id
                INNER JOIN t_activity a ON osa.activity_id = a.id
                AND tas.activity_id = a.id
                WHERE
                    o.`status` = 1
                AND (
                    (
                        osa.activity_type = 2
                        AND tas.reserved_inventory = 0
                    )
                    OR (
                        osa.activity_type = 1
                        AND a.activity_status = 2
                    )
                )
) t
);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for showTagActivitySkuChildList
-- ----------------------------
DROP PROCEDURE IF EXISTS `showTagActivitySkuChildList`;
DELIMITER ;;
CREATE DEFINER=`ec`@`%` PROCEDURE `showTagActivitySkuChildList`(IN rootId VARCHAR(20),IN level INT,IN `limit` INT)
BEGIN
    DROP TEMPORARY TABLE IF EXISTS tagSkuTmpList;
    CREATE TEMPORARY TABLE tagSkuTmpList(`SNO` INT(11) NOT NULL AUTO_INCREMENT,`ID` varchar(20),`DEPTH` INT(11),`ISSKUTAG` INT(11), PRIMARY KEY (`SNO`));

    CALL tagSkuChildList(rootId,1,level,`limit`);

        SELECT
			A.*,(T.DEPTH - 1)depth,
			T.ISSKUTAG isSkuTag,
			B.id p_id,
			B. NAME p_name,
			B.parent_id p_parent_id,
			B.invalid p_invalid,
			B.rank p_rank,
			B.icon p_icon,
			B.create_time p_create_time,
			B.update_time p_update_time,
			S.id s_id,
			S.spu_id s_spu_id,
			S. NAME s_name,
			S.description s_description,
			S.detail s_detail,
			S.erp_code s_erp_code,
			S.head_thumbnail s_head_thumbnail,
			S.images_thumbnail s_images_thumbnail,
			S.images_original s_images_original,
			S.invalid s_invalid,
			S.original_price s_original_price,
			(case when ac.activity_status=1 then ac.activity_price else  S.price end ) s_price,
			S.installment s_installment,
			S.is_default s_is_default,
			S.create_time s_create_time,
			S.update_time s_update_time,
			ac.activity_price   ac_activity_price,
			ac.activity_id ac_activity_id,
			ac.activity_type ac_activity_type, 
			ac.activity_status ac_activity_status
			FROM
			(tagSkuTmpList T, t_tag A)
			LEFT JOIN t_tag B ON A.parent_id = B.id
			LEFT OUTER JOIN t_tag_sku C ON C.tag_id = A.id
			AND T.ISSKUTAG = 1
			AND C.invalid = 0
			LEFT JOIN t_sku S ON C.sku_id = S.id
			AND S.invalid = 0
			AND S.`status` = 1
			LEFT JOIN (SELECT
					tas.sku_id,
					tas.activity_price,
					tas.activity_id,
					ta.activity_type,
          ta.activity_status
				FROM
					t_activity ta
				LEFT JOIN t_activity_sku tas 
        ON ta.id = tas.activity_id 
        and ta.activity_status=1) as ac 
      on S.id=ac.sku_id
			WHERE
			T.ID = A.id
			AND A.parent_id != 0
			AND A.invalid = 0
			AND A. STATUS = 1
			ORDER BY
			T.SNO,
			C.rank;
    END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for showTagChildIds
-- ----------------------------
DROP PROCEDURE IF EXISTS `showTagChildIds`;
DELIMITER ;;
CREATE DEFINER=`ec`@`%` PROCEDURE `showTagChildIds`(IN rootId VARCHAR(20))
BEGIN
	DROP TEMPORARY TABLE IF EXISTS tagTmpList;
	CREATE TEMPORARY TABLE tagTmpList(`SNO` INT(11) NOT NULL AUTO_INCREMENT,`ID` varchar(20),`DEPTH` INT(11), PRIMARY KEY (`SNO`));

	CALL tagChildList(rootId,1,10);

	SELECT T.ID FROM tagTmpList T;

	END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for showTagChildList
-- ----------------------------
DROP PROCEDURE IF EXISTS `showTagChildList`;
DELIMITER ;;
CREATE DEFINER=`ec`@`%` PROCEDURE `showTagChildList`(IN rootId VARCHAR(20),IN level INT)
BEGIN
	DROP TEMPORARY TABLE IF EXISTS tagTmpList;
	CREATE TEMPORARY TABLE tagTmpList(`SNO` INT(11) NOT NULL AUTO_INCREMENT,`ID` varchar(20),`DEPTH` INT(11), PRIMARY KEY (`SNO`));

	CALL tagChildList(rootId,1,level);

	SELECT A.*,(T.DEPTH-1) depth,B.id p_id,B.name p_name,B.parent_id p_parent_id,B.invalid p_invalid,B.rank p_rank,B.icon p_icon,B.create_time p_create_time,B.update_time p_update_time FROM tagTmpList T,t_tag A left join t_tag B on A.parent_id = B.id WHERE T.ID = A.id and A.parent_id != 0 and A.invalid = 0 and A.`status` = 0 ORDER BY T.SNO;

	END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for showTagSkuChildList
-- ----------------------------
DROP PROCEDURE IF EXISTS `showTagSkuChildList`;
DELIMITER ;;
CREATE DEFINER=`ec`@`%` PROCEDURE `showTagSkuChildList`(IN rootId VARCHAR(20),IN level INT,IN `limit` INT)
BEGIN
    DROP TEMPORARY TABLE IF EXISTS tagSkuTmpList;
    CREATE TEMPORARY TABLE tagSkuTmpList(`SNO` INT(11) NOT NULL AUTO_INCREMENT,`ID` varchar(20),`DEPTH` INT(11),`ISSKUTAG` INT(11), PRIMARY KEY (`SNO`));

    CALL tagSkuChildList(rootId,1,level,`limit`);

        SELECT A.*,(T.DEPTH-1) depth,T.ISSKUTAG isSkuTag,B.id p_id,B.name p_name,B.parent_id p_parent_id,B.invalid p_invalid,B.rank p_rank,B.icon p_icon,B.create_time p_create_time,B.update_time p_update_time,S.id s_id,S.spu_id s_spu_id,S.name s_name,S.description s_description,S.detail s_detail,S.erp_code s_erp_code,S.head_thumbnail s_head_thumbnail,S.images_thumbnail s_images_thumbnail,S.images_original s_images_original,S.invalid s_invalid,S.original_price s_original_price,S.price s_price,S.installment s_installment,S.is_default s_is_default,S.create_time s_create_time,S.update_time s_update_time FROM (tagSkuTmpList T,t_tag A) left join t_tag B on A.parent_id = B.id left outer join t_tag_sku C on C.tag_id = A.id and T.ISSKUTAG = 1 and C.invalid = 0 left join t_sku S on C.sku_id = S.id and S.invalid = 0 and S.`status`=1 WHERE T.ID = A.id and A.parent_id != 0 and A.invalid=0 and A.status=1  ORDER BY T.SNO, C.rank;

    END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for skuPro
-- ----------------------------
DROP PROCEDURE IF EXISTS `skuPro`;
DELIMITER ;;
CREATE DEFINER=`ec`@`%` PROCEDURE `skuPro`()
BEGIN
UPDATE t_sku u
LEFT JOIN (
    SELECT
        ak.sku_id AS skuId,
        s.`status` AS skuStatus,
        a.start_time AS startTime,
        a.activity_status AS activityStatus
    FROM
        t_activity a
    LEFT JOIN t_activity_sku ak ON a.id = ak.activity_id
    LEFT JOIN t_sku s ON s.id = ak.sku_id
) AS m ON u.id = m.skuId
SET u.`status` = 1
WHERE
    m.skuStatus = 0
AND m.startTime = NOW()
AND m.activityStatus = 0;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for tagChildList
-- ----------------------------
DROP PROCEDURE IF EXISTS `tagChildList`;
DELIMITER ;;
CREATE DEFINER=`ec`@`%` PROCEDURE `tagChildList`(IN rootId VARCHAR(20),IN nDepth INT,IN level INT)
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE b VARCHAR(20);

	DECLARE cur1 CURSOR FOR SELECT a.id FROM t_tag a WHERE a.parent_id = rootId and a.invalid = 0 ORDER BY a.rank;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	SET max_sp_recursion_depth=12;

	INSERT INTO tagTmpList VALUES (NULL,rootId,nDepth);

	OPEN cur1;

	FETCH cur1 INTO b;
	WHILE done=0 DO
		IF level >= nDepth
		THEN
			CALL tagChildList(b,nDepth+1,level);
			FETCH cur1 INTO b;
		ELSE
			FETCH cur1 INTO b;
		END IF;
	END WHILE;

	CLOSE cur1;

	END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for tagSkuChildList
-- ----------------------------
DROP PROCEDURE IF EXISTS `tagSkuChildList`;
DELIMITER ;;
CREATE DEFINER=`ec`@`%` PROCEDURE `tagSkuChildList`(IN rootId VARCHAR(20),IN nDepth INT,IN level INT,IN `limit` INT)
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE b VARCHAR(20);

	DECLARE cur1 CURSOR FOR SELECT a.id FROM t_tag a WHERE a.parent_id = rootId and a.invalid = 0 ORDER BY a.rank;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	SET max_sp_recursion_depth=12;

	IF level >= nDepth
		THEN
			INSERT INTO tagSkuTmpList VALUES (NULL,rootId,nDepth,0);
	ELSE
			INSERT INTO tagSkuTmpList VALUES (NULL,rootId,nDepth,1);
		END IF;

	OPEN cur1;

	FETCH cur1 INTO b;
	WHILE done=0 DO
		IF level >= nDepth
		THEN
			CALL tagSkuChildList(b,nDepth+1,level,`limit`);
			FETCH cur1 INTO b;
		ELSE
			FETCH cur1 INTO b;
		END IF;
	END WHILE;

	CLOSE cur1;

	END
;;
DELIMITER ;