/**
 * Created by user on 23/10/2016.
 */
var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');
var bodyParser = require("body-parser");
var express = require('express');
var cors = require('cors');
var app = express();
var url = 'mongodb://toadSTL:toadSTL1@ds135993.mlab.com:35993/ase-icp-9';
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.post('/insert', function (req, res) {
    MongoClient.connect(url, function(err, db) {
        if(err)
        {
            res.write("Connection Failed, Error while connecting to Database");
            res.end();
        }
        insertDoc(db, req.body, function() {
            res.write("Successfully Inserted");
            res.end();
        });
    });
});
var insertDoc = function(db, data, callback) {
    db.collection('users').insertOne( data, function(err, result) {
        if(err)
        {
            res.write("Insert Failed, Error while inserting document");
            res.end();
        }
        console.log("Inserted document into the 'users' collection.");
        callback();
    });
};


app.post('/search', function (req, res, next) {
    var resultArray = [];
    MongoClient.connect(url, function(err, db) {
        assert.equal(null, err);;
        console.log(req.body)
        var cursor = db.collection('users').find(req.body);
        cursor.forEach(function(doc, err) {
            assert.equal(null, err);
            console.log("Search Results");
            console.log("First Name: " + doc.fname);
            console.log("Last Name: " + doc.lname);
            console.log("ClassID: " + doc.classID);
            console.log("Course: " + doc.course);
            console.log("Major: " + doc.major);
            console.log("Major: " + doc.minor);
            resultArray.push(doc);
        }, function () {
            db.close();
            res.send({items: resultArray});
        });
    });
});


var server = app.listen(8081,function () {
    var host = server.address().address;
    var port = server.address().port;
    console.log("Example app listening at http://%s:%s", host, port)
});