/**
 * Created by user on 23/10/2016.
 */
var myapp = angular.module('demoMongo',[]);
myapp.run(function ($http) {
    // Sends this header with any AJAX request
    $http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
    // Send this header only in post requests. Specifies you are sending a JSON object
    $http.defaults.headers.post['dataType'] = 'json'
});

myapp.controller('MongoRestController',function($scope,$http){
    $scope.insertData = function(){
        console.log($scope.formData.lname);
        console.log($scope.formData.fname);
        console.log($scope.formData.classID);
        console.log($scope.formData.course);
        console.log($scope.formData.major);
        console.log($scope.formData.minor);
        //Class ID, Student name, Course of study, Major and Minor
        var req = $http.post('http://127.0.0.1:8081/insert',$scope.formData);
        req.success(function(data) {
            console.log(data);
            alert("Successfully Inserted!")
        });
        req.error(function(data) {
            alert( "failure message: " + JSON.stringify({data: data}));
        });
    };

    $scope.searchData = function(){
        var val = $scope.formData.value;
        var name = $scope.formData.name;
        var searchData = { [name]: val };
        console.log(searchData);
        var req = $http.post('http://127.0.0.1:8081/search',searchData);
        //console.log(req)
        req.success(function(data) {
            var results = [];
            results.push({results: "<h1>Search Results</h1>"});
            for (var i = 0; i < data.items.length; i++) {
                firstName = data.items[i].fname;
                lastName = data.items[i].lname;
                classID = data.items[i].classID;
                course = data.items[i].course;
                major = data.items[i].major;
                minor = data.items[i].minor;
                results.push({
                    results: 'First Name: ' + firstName + '<br>'+
                        "Last Name: " + lastName + '<br>'+
                        "ClassID: " + classID + '<br>'+
                        "Course: " + course + '<br>'+
                        "Major: " + major + '<br>'+
                        "Minor: " + minor + '<br>'
                })
                results.push({
                    results: '<br>'
                })
            }
            //console.log(results);
            $scope.itemResults = results;
        });
        req.error(function(data) {
            alert( "failure message: " + JSON.stringify({data: data}));
        });
    };
});

