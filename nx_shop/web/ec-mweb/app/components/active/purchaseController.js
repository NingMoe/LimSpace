angular
	.module('app')
	.controller('purchaseController', purchaseController);

purchaseController.$inject = ['$scope', '$api'];

function purchaseController($scope, $api) {
	var events = {};
  $scope.activityStatus ="3";
  $api.get("activity/activityType/2").then(function(resp){
    if(resp.data.code ==200){
    	$.each(resp.data.activityList,function(index,item){
    		if(item.activityStatus ==0){
          item.status ="未开始" ;
    		}else if (item.activityStatus ==1){
           item.status ="进行中" ;
    		}else if(item.activityStatus ==2){
    			 item.status ="已结束" ;
    		}
    	})
      $scope.purchaselist =resp.data.activityList;
    }else{
    	alert(resp.data.message)
    }
  });

  events.activity = function (x){
  	$api.put("activity/"+x.id+"").then(function(resp){
        if(resp.data.code ==200){
          location.reload();
        }else{
        	alert(resp.data.message)
        }
  	});
  }

  events.delete = function (x){
  	$api.delete("activity/"+x.id+"").then(function(resp){
        if(resp.data.code ==200){
          location.reload();
        }else{
        	alert(resp.data.message)
        }
  	});
  }

  events.remarks =function(x){
    $('#prompt').modal('show');
    $scope.textremarks = x.activityRemarks;
  }

  events.look = function (x){
     $('#look').modal('show');
     $scope.setlook =true;
     if(x.activityStatus == 1){
     	 $scope.setlook =false;
     }
     $scope.thisid = x.id;
     $api.get("activitySku/list/"+x.id+"").then(function(resp){
     	 $.each(resp.data.activitySkuList,function(index,item){
     	 	  if(!item.soldNum){
     	 	  	item.soldNum = 0;
     	 	  }
          item.newinventory = item.soldNum + item.reservedInventory;
     	 })
     	 $scope.activitySkuList = resp.data.activitySkuList;
     });
  }

  events.add = function (x){
  	 $('#numberadd').modal('show');
  	 $scope.thissku = x;
  }
  
  events.numberadd = function (x){
  	if(x.inventory < $scope.addnumber ){
  		 alert("补仓数不能大于剩余总库存数");
  		 return;
  	}
    if(x.reservedInventory <= 0){
       alert("剩余预留库存数为0 不能进行补仓");
       return;
    }
  	$api.put("activitySku/"+x.id+"/"+$scope.addnumber+"").then(function(resp){
        if(resp.data.code ==200){
         $('#numberadd').modal('hide');
		     $api.get("activitySku/list/"+$scope.thisid+"").then(function(resp){
		     	 $.each(resp.data.activitySkuList,function(index,item){
		     	 	  if(!item.soldNum){
		     	 	  	item.soldNum = 0;
		     	 	  }
            item.newinventory = item.soldNum + item.reservedInventory;
		     	 })
		     	 $scope.activitySkuList = resp.data.activitySkuList;
		     });
        }else{
        	alert(resp.data.message)
        }
  	});
  }
  
  events.activityStatus = function (){
    $api.get("activity/list",{activityType:2,activityStatus:$scope.activityStatus}).then(function(resp){
      if(resp.data.code ==200){
        $.each(resp.data.activityList,function(index,item){
          if(item.activityStatus ==0){
            item.status ="未开始" ;
          }else if (item.activityStatus ==1){
             item.status ="进行中" ;
          }else if(item.activityStatus ==2){
             item.status ="已结束" ;
          }
        })
        $scope.purchaselist =resp.data.activityList;
      }else{
        alert(resp.data.message)
      }
    });
  }

  events.seckillsear = function(){
    $scope.activityStatus ="3";
    if($scope.activityName){
      $api.get("activity/list",{activityType:2,activityName:$scope.activityName}).then(function(resp){
        if(resp.data.code ==200){
          $.each(resp.data.activityList,function(index,item){
            if(item.activityStatus ==0){
              item.status ="未开始" ;
            }else if (item.activityStatus ==1){
               item.status ="进行中" ;
            }else if(item.activityStatus ==2){
               item.status ="已结束" ;
            }
          })
          $scope.purchaselist =resp.data.activityList;
        }else{
          alert(resp.data.message)
        }
      });
    }else{
      $api.get("activity/activityType/2").then(function(resp){
        if(resp.data.code ==200){
          $.each(resp.data.activityList,function(index,item){
            if(item.activityStatus ==0){
              item.status ="未开始" ;
            }else if (item.activityStatus ==1){
               item.status ="进行中" ;
            }else if(item.activityStatus ==2){
               item.status ="已结束" ;
            }
          })
          $scope.purchaselist =resp.data.activityList;
        }else{
          alert(resp.data.message)
        }
      });
    }

  }
   $scope.events = events;
}