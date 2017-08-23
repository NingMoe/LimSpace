angular
  .module('app')
  .controller('createActivityController', createActivityController);
createActivityController.$inject = ['$scope', '$rootScope', '$location', '$state','$api'];
function createActivityController($scope, $rootScope, $location, $state,  $api) {
  

  $("#receiveStartTime").datetimepicker({format:"Y-m-d H:i",step:1});
  $("#receiveendTime").datetimepicker({format:"Y-m-d H:i",step:1});
  

  function paddingZero(num) {
    return num >= 10 ? num : '0' + num;
  }

  function formatDataTime(time) {
    if (!time) {
      return '';
    }
    var date = new Date(time);
    var result = date.getFullYear() + '-' + paddingZero(date.getMonth() + 1) + '-' + paddingZero(date.getDate()) +' '+ paddingZero(date.getHours()) +':'+paddingZero(date.getMinutes())+':'+paddingZero(date.getSeconds());
    return result;
  }

  var events = {};
  $scope.activityType = "2";
  $scope.endTimehiden = true;
 
  $scope.fristOnehide = false;
  $scope.fristTwohide = true;
  $scope.cheboxbtn =true;
  $scope.select1val = "请选择";
  $scope.select2val = "请选择";
  $scope.select3val = "请选择";
  $scope.statusSku = "2";
  $scope.activitydis =false;



    if(location.href.split("?")[1]){
      $scope.activitydis =true;
      $api.get("activity/"+location.href.split("?")[1]+"").then(function(resp){
         $scope.activityName = resp.data.activity.activityName;
         $scope.activityType = resp.data.activity.activityType+"";
         if(resp.data.activity.activityMode ==1){
            $scope.person ={
              purchase:1
            }
         }else if(resp.data.activity.activityMode ==2){
            $scope.person ={
              purchase:2
            }
         }
         $scope.startTime =resp.data.activity.startTime;
         $scope.activityRemarks = resp.data.activity.activityRemarks;
         $scope.person1 = resp.data.activity.coupon;
         if(resp.data.activity.activityType ==1){
             $scope.personPurchase =true;
             $scope.endTimehiden =false;
             $scope.endTime = resp.data.activity.endTime;
         }
         if(resp.data.activity.coupon ==1){
            $scope.person01 =true;

         }else if(resp.data.activity.coupon ==2){
            $scope.person02 =true;
         }
         if(resp.data.activity.coupon.split(",")[0] && resp.data.activity.coupon.split(",")[1]){
            $scope.person01 =true;
            $scope.person02 =true;
         }
      });
    }
  events.Nextstep1  = function(form){
     if(!$scope.activityName){
      alert("请输入活动名称");
      return false;
     }
     if(!$scope.startTime){
      alert("请输入活动开始时间");
      return false;
     }else if(new Date($scope.startTime) < new Date()){     //限制只能创建半小时后活动 new Date().valueOf() + 0.5*60*60*1000
      alert("活动开始时间必须大于当前时间");
      return false;
     }
    if($scope.activityType ==1){
      if(!$scope.endTime){
        alert("请输入活动结束时间");
        return false;
      }else if(new Date($scope.endTime) < new Date($scope.startTime)){
        alert("结束时间大于开始时间");
        return false;
      }
    }
    var coupon="0";
    if($scope.person01){
       coupon="1";
    }else if($scope.person02){
       coupon="2";
    }
    if($scope.person01 && $scope.person02){
       coupon="1,2";
    }
    $scope.submitnext =true;
    var data ={
      activityName:$scope.activityName,
      activityType:$scope.activityType,
      activityMode:2,
      startTime:formatDataTime($scope.startTime),
      endTime:formatDataTime($scope.endTime) || "" ,
      activityRemarks:$scope.activityRemarks || "",
      coupon:coupon
     }
     $scope.activityTypeIdentify = $scope.activityType; //1秒 2限

    if(location.href.split("?")[1]){
      data.id = location.href.split("?")[1];
      $api.putJson("activity",data).then(function(resp){
        $scope.submitnext = false;
        if(resp.data.code ==200){
           $scope.activityId = data.id;
           $scope.fristOnehide = true;
           $scope.fristTwohide = false;
           $api.get('activitySku/'+location.href.split("?")[1]+'').then(function(resp){
             $.each(resp.data.skuList,function(index,item){
               item.skudisabled = false
             })
             $scope.skulist = resp.data.skuList;
           })
           $api.get("activitySku/list/"+location.href.split("?")[1]+"").then(function(resp){
            
            $.each(resp.data.activitySkuList,function(index,item){
                item.name = item.skuName;
                item.x_pricemax = item.discount;
                item.x_pricemin = item.discountedPrice;
                item.reservedInventory = item.reservedInventory;
                item.purchaseNum = item.purchaseNum;
                item.inventory = item.inventory + item.reservedInventory;
                item.discount1 = item.activityPrice;
                item.toRank = item.toRank;
                // if(item.discount){
                //  item.discount1 = (item.price * (item.discount/10)).toFixed(2) ;
                // }else if(item.discountedPrice){
                //   item.discount1 = (item.price -  item.discountedPrice).toFixed(2);
                // }
            })
            var newdata ={};
            $.each(resp.data.activitySkuList,function(index,item){
              newdata[index] = item;
            })
            $scope.newskulist = newdata
            console.log($scope.newskulist)
           });
        }else{
          alert(resp.data.message)
        }
      });
    }else{
      $api.postJson("activity",data).then(function(resp){
        $scope.submitnext = false;
        if(resp.data.code ==200){
           $scope.activityId = resp.data.activityId;
           $scope.fristOnehide = true;
           $scope.fristTwohide = false;
           $api.get('activitySku/'+$scope.activityId+'').then(function(resp){
             $.each(resp.data.skuList,function(index,item){
               item.skudisabled = false
             })
             $scope.skulist = resp.data.skuList;
           })
        }else{
          alert(resp.data.message)
        }
      });
    }
  }




  events.activityTypechang = function(){
    if($scope.activityType ==2){
      $scope.endTimehiden = true;
    }else if($scope.activityType ==1){
      $scope.endTimehiden = false;
    }
  }

  events.trclick = function(data,clickthis){
   
    if(!data.checkedlist){
      if(!data.skudisabled){
        data.checkedlist = true;
        $scope.cheboxbtn =false;  
      }
    }else{
        data.checkedlist =false;
        var aa =true;
        $.each($scope.skulist,function(index,item){
          if(item.checkedlist){
           $scope.cheboxbtn =false;
           aa =false;  
          }else{
            if(aa){
               $scope.cheboxbtn =true;  
            }
           
          }
        })

    }
  }


  events.checkboxall = function(){
    if($scope.parentCheck){
      $.each($scope.skulist,function(index,item){
        if(!item.skudisabled){
          item.checkedlist = true;
          $scope.cheboxbtn =false;       
        }
      })
    }else{
      $.each($scope.skulist,function(index,item){
        if(!item.skudisabled){
          item.checkedlist = false;
          $scope.cheboxbtn =true;       
        }
      })
    }
  }
 
  events.cheboxclick = function(){
     var newdata ={};
      if(location.href.split("?")[1]){
          $.each($('.over-css table tr td input:checked'),function(index,item){
            if(!JSON.parse($(item).val()).inventory || !JSON.parse($(item).val()).price){
              alert("数据异常无法参加活动")
               return false;
            }else{
              newdata[index] = JSON.parse($(item).val());
              $.each($scope.skulist,function(index1,item1){
                 if(JSON.parse($(item).val()).id == item1.id){
                   item1.skudisabled = true;
                   item1.checkedlist = false;
                 }
              })
            }
          });
          $.each($scope.newskulist,function(index,item){
            newdata[parseFloat(index)+parseFloat($('.over-css table tr td input:checked').length)] = item;
          })
          $.each(newdata,function(index,item){
            if(!item.skuId){
               item.skuId = item.id;
            }
          })
          $scope.newskulist = newdata ;
          console.log($scope.newskulist)
      }else{
        $.each($('.over-css table tr td input:checked'),function(index,item){
          if(!JSON.parse($(item).val()).inventory || !JSON.parse($(item).val()).price){
              alert("数据异常无法参加活动");
              return false;
          }else{
            newdata[index] =JSON.parse($(item).val());
            $.each($scope.skulist,function(index1,item1){
               if(JSON.parse($(item).val()).id == item1.id){
                 item1.skudisabled = true;
                 item1.checkedlist = false;
               }
            })
          }
        });
          $.each($scope.newskulist,function(index,item){
            newdata[parseFloat(index)+parseFloat($('.over-css table tr td input:checked').length)] = item;
          })
          $.each(newdata,function(index,item){
            if(!item.skuId){
               item.skuId = item.id;
            }
          })
      }
      $scope.newskulist =newdata ;
  }

  events.priceset = function(){
    if(parseFloat($scope.setcount) < parseFloat($scope.setnumber)){
      alert("限购数量不能大于预设库存");
      return;
    }else if(parseFloat($scope.setpricemax) >= 10){
      alert("折扣金额不能大于等于10");
      return;
    }
    $.each($scope.newskulist,function(index,item){
       item.activityId = $scope.activityId;
       //7. 限购数必须填写，限购数值不能大于预设库存。
       if($scope.setcount >= item.inventory){
         item.reservedInventory =  item.inventory
       }else{
         item.reservedInventory = $scope.setcount;
       }

       //批量设置时 折扣金额
       if($scope.setpricemax || $scope.setpricemin ){
         if(parseFloat($scope.setpricemax)){
              item.x_pricemax =  $scope.setpricemax;
              item.discount1 = (item.price * ( item.x_pricemax/10)).toFixed(2);
              item.x_pricemin =  (item.price - (item.price * ( item.x_pricemax/10))).toFixed(2);
         }
         if(parseFloat($scope.setpricemin)){ //11.  直降金额不能大等于商品金额 批量设置时 直降金额金额大于实际商品金额时  折扣金额为0
            if($scope.setpricemin >= item.price){
              item.x_pricemin = "";
              item.x_pricemax = "";
              item.discount1 = "";
            }else{
              item.x_pricemin = $scope.setpricemin;
              item.discount1 = (item.price - item.x_pricemin).toFixed(2) ;
              item.x_pricemax = ((item.price - item.x_pricemin) /item.price * 10).toFixed(2);
            }  
         }
       }else{
          item.x_pricemin = "";
          item.x_pricemax = "";
          item.discount1 = "";
       }
        //8.  预设库存数不能大于当前库存数，不填写预设库存则为使用原库存
       if(parseFloat($scope.setnumber) >= parseFloat(item.reservedInventory)){
         item.purchaseNum = item.reservedInventory;
       }else{
         item.purchaseNum =  $scope.setnumber;
       }

    })

  }
  events.select1 = function (){
      $scope.select2val="请选择";
      $scope.select3val="请选择";
      $scope.nodes2="";
      $scope.nodes3="";
      $scope.statusSku ="2"
    if($scope.select1val =="请选择"){

       $api.get('activitySku/'+$scope.activityId+'').then(function(resp){
         $.each(resp.data.skuList,function(index,item){
           item.skudisabled = false
         })
         $scope.skulist = resp.data.skuList;
       })
      return;
    }
    $api.get("categories/"+$scope.select1val+"/nodes").then(function(resp){
        $scope.nodes2  =resp.data.data;
    });
    var data = {
      id:$scope.activityId,
      skuId:"",
      categoryId:$scope.select1val || "",
      status:"",
    }
    searsku(data);
  }

  events.select2 = function (){
      $scope.nodes3="";
      $scope.select3val="请选择";
      $scope.statusSku ="2"
    if($scope.select2val =="请选择"){
      return;
    }
    $api.get("categories/"+$scope.select2val+"/nodes").then(function(resp){
        $scope.nodes3  =resp.data.data;
    });
    var data = {
      id:$scope.activityId,
      skuId:"",
      categoryId:$scope.select2val || "",
      status:"",
    }
    searsku(data);
  }

  events.select3 = function (){
    $scope.statusSku ="2"
    var data = {
      id:$scope.activityId,
      skuId:"",
      categoryId:$scope.select3val || "",
      status:"",
    }
    searsku(data);
  }

  events.statusSku =function(){
    var categoryId="";
    if($scope.select1val !="请选择"){
      categoryId = $scope.select1val;
    }
    if($scope.select2val !="请选择"){
      categoryId = $scope.select2val;
    }
    if($scope.select3val !="请选择"){
      categoryId = $scope.select3val;
    }
    var data = {
      id:$scope.activityId,
      skuId:"",
      categoryId:categoryId,
      status:$scope.statusSku || "",
    }
    searsku(data);
  }

  events.skusear = function(){
    var data = {
      id:$scope.activityId,
      skuId:$scope.skuId || "",
      categoryId:"",
      status:"",
    }
    searsku(data);
  }

  function searsku(data){
     $api.get('activitySku',data).then(function(resp){
       $scope.skulist = resp.data.skuList;
     })
  }

  events.discount = function (x){
    //输入折扣时 自动算出优惠后价格和直降金额 2016-08-10 14:30 测试提供
    if(parseFloat(x.x_pricemax) > 9){
      x.x_pricemax = 9;
    }
    if(parseFloat(x.x_pricemax)){
      x.x_pricemin = (x.price - (x.price * ( x.x_pricemax/10))).toFixed(2);
      x.discount1 = (x.price * ( x.x_pricemax/10)).toFixed(2) ;
    }else{
      x.x_pricemin="";
      x.discount1="";
      x.x_pricemax=""
    }
  }

  events.setDiscount =function (){
    $scope.setpricemin ="";
  }

  events.plummets = function (x){
    //输入直降时 自动算出优惠后价格和折扣 2016-08-10 14:30 测试提供
    if(parseFloat(x.x_pricemin) >= x.price){
      alert("直降金额不能大于等于商品金额");
      x.x_pricemin ="";
      x.x_pricemax = "";
      x.discount1 ="";
      return false;
    }
    if(parseFloat(x.x_pricemin)){
       x.discount1 = (x.price - x.x_pricemin).toFixed(2);
       x.x_pricemax = ((x.price - x.x_pricemin) /x.price * 10).toFixed(2);
    }else{
      x.x_pricemin="";
      x.discount1="";
      x.x_pricemax=""
    }

  }

  events.setPlummets = function (){
     $scope.setpricemax ="";
  }

  events.preset = function (x){
    if(parseFloat(x.reservedInventory) > parseFloat(x.inventory)){
       x.reservedInventory = x.inventory;
    }
    if(x.purchaseNum){
       if(parseFloat(x.purchaseNum) > parseFloat(x.reservedInventory)){
         x.purchaseNum =  x.reservedInventory
       }
    }
  }
  
  events.restriction = function (x){
    if(parseFloat(x.purchaseNum) > parseFloat(x.inventory) ){
       x.purchaseNum =  x.inventory;

    }
    if(x.reservedInventory){
       if(parseFloat(x.purchaseNum) > parseFloat(x.reservedInventory)){
         x.purchaseNum =  x.reservedInventory
        }
    }
  }

  events.deldete = function (x){
     $.each($scope.newskulist,function(index,item){
       if(x.id == item.id){
         delete $scope.newskulist[index];
       }
     })
    $.each($scope.skulist,function(index1,item1){
       if(x.id == item1.id){
         item1.skudisabled = false;
       }
    })
  }
  var btnSetInterval = false;
  events.submitNext2 = function (){
    var activitySku = [];
    var activitySkulist= {};
    var returnfalse = false;
    if(btnSetInterval){
      return false;
    }
    btnSetInterval =true;
    setTimeout(function(){
      btnSetInterval= false;
    },2000)


    $.each($scope.newskulist,function(index,item){
        //1秒 2限
        if(!item.x_pricemax || item.x_pricemax == 0){
          if(!item.x_pricemin || item.x_pricemin == 0){
           alert("SKU编码为"+ item.skuId +"优惠折扣或优惠金额为空!");
           returnfalse = true;
           return false;
         }
        }
        if(!item.reservedInventory){
           alert("SKU编码为"+ item.skuId +"预设库存为空!");
           returnfalse = true;
           return false;
        }
        if(!item.topRank){
		       alert("SKU编码为"+ item.skuId +"排序为空!");
           returnfalse = true;
           return false;
        }
        if(!isRepeat(item)){
        	 returnfalse = true;
           return false;
        }
        if(!item.inventory){
           alert("SKU编码为"+ item.skuId +"库存数据异常!");
           returnfalse = true;
           return false;
        }
        if(item.reservedInventory > item.inventory){
           alert("SKU编码为"+ item.skuId +"预留库存数大于总库存数!");
           returnfalse = true;
           return false;
        }else if(!item.reservedInventory){
           alert("SKU编码为"+ item.id +"预留库存个数为空!");
           returnfalse = true;
           return false;       
        }
       if($scope.activityTypeIdentify ==2){
          if(item.purchaseNum){
            if(parseFloat(item.purchaseNum) > parseFloat(item.reservedInventory)){
             alert("SKU编码为"+ item.skuId +"限购库存数大于预留库存数!");
             returnfalse = true;
             return false;
            }
          }else if(!item.purchaseNum){//            限购数可不填写，不填写为不限购。限购数值不能大于预设库存。
             alert("SKU编码为"+ item.id +"限购个数为空!");
             returnfalse = true;
             return false;
          }
        }

        activitySkulist = {
          activityId:$scope.activityId,
          skuId:item.id,
          skuName:item.name,
          price:item.price,
          activityPrice:item.discount1 || "",
          inventory:item.reservedInventory || "",
          reservedInventory:item.reservedInventory || "" ,
          discountedPrice:item.x_pricemin || "",
          discount:item.x_pricemax || "",
          purchaseNum:item.purchaseNum || "",
          topRank:item.topRank|| ""
        }
        if(location.href.split("?")[1]){
          if(item.skuId){
            activitySkulist.skuId = item.skuId 
          }
           activitySkulist.activityId =  location.href.split("?")[1];
        }
        activitySku.push(activitySkulist)
    })
    if(returnfalse){
       return false;
    }
    console.log({activitySku:JSON.stringify(activitySku)})
    $api.post("activitySku",{activitySku:JSON.stringify(activitySku)}).then(function(resp){
        if(resp.data.code ==200){
           if($scope.activityTypeIdentify ==1){//1秒 2限
             location.href="#/seckill";
           }else if($scope.activityTypeIdentify ==2){
             location.href="#/purchase";
           }
        }else{
          alert(resp.data.message)
        }
    });
  }
  $api.get("categories/0/nodes").then(function(resp){
    $scope.nodes1  =resp.data.data;
  });
//function isRepeat(x){
//	s.each($scope.newskulist,function(item){
//		alert(item.topRank);
//		if(x.topRank=item.topRank&&x.skuId!=item.skuId){
//			alert("skuID为"+x.skuId+"与"+item.skuId+"重复！");
//			return false;
//		}
//	});
//	return true;
//}
  function isRepeat(thisdata){
  	var isRepeat = true;
  	$.each($scope.newskulist,function(index,item){
  		if(Number(thisdata.topRank) ==item.topRank && thisdata.skuId != item.skuId){
  			alert("SKU编码为"+thisdata.skuId+"与编码为"+item.skuId+"重复！");
  			isRepeat= false;
  		};
  	});
  	return isRepeat;
  }
  
  $scope.events = events;
}
