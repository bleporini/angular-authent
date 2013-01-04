function LoginCtrl($scope, $http,$log, $location){
    $scope.user={mail:'tony@stark.com',password:'ironman'}
    $scope.loginError=false

    $scope.authenticate = function(user){
        $http.post("/login", user).
            success(function(data, status, headers, config){
                $log.log("status: " + status + " data: ")
                $log.log(data)
                $location.path("/")
            }).
            error(function(data, status, headers, config){
                $log.error(" error status: " + status + " data: " + data)
                $scope.loginError=true
            })
    }


}


function ProtectedCtrl($scope,$http,$log,$location){
    $scope.payload=new Object()
    $http.get("/protectedResource").
        success(function(data,status,headers,config){
            $log.info("status: " + status + " data: ")
            $log.info(data)
            $scope.payload = data
        }).
        error(function(data,status,headers,config){
            $log.error("status: " + status + " data: ")
            $log.error(data)
        })

    $scope.logout = function(){
        $http.get("/logout").
            success(function(data,status,headers,config){
                $location.path("/login")
            })
    }
}