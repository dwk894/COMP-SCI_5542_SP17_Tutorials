<%--
  Created by IntelliJ IDEA.
  User: dwk89
  Date: 03/23/2017
  Time: 06:05:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Standard Emoji Impression Recognition</title>
  <script type = 'text/javascript' src = 'https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js'></script>
  <script type = 'text/javascript' src = 'https://rawgit.com/dwk8923/Miscellaneous_2/master/vv/angularJS_min.js'></script>
</head>
<body style = 'background-color: beige; font-family: Trebuchet MS;'>
<p align = 'center' style = 'color: darkred;'>COMP-SCI 5542 (SP17) - Big Data Analytics and Applications</p>
<p align = 'center' style = 'font-size: 1.5em; color: darkred;'><u>Tutorial 9 Assignment - Question 2</u></p>
<p align = 'center' style = 'font-size: 1.5em; color: red'>Standard Emoji Impression Recognition</p>
<p align = 'center' style = 'color:darkred;'><b><i>Dayu Wang</i></b> (45)</p>
<form action = 'qa'>
  <p style = 'color: darkgreen;'>Please give the URL of the <strong>emoji impression</strong>:<br><br><input id = 'url'></p>
  <p style = 'color: darkgreen;'>Please give a text question about the emoji:<br><br><input id = 'question'></p>
  <button type = 'submit' id = 's'>Submit</button>
</form>
<p id = 'res'></p>
</body>
</html>
