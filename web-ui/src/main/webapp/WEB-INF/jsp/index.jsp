<%--
  ~ Copyright (c) 2010-2012. Axon Framework
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<html>
<head>
    <title>Welcome to the axon trader</title>
</head>
<body>
<content tag="title">Welcome</content>
<content tag="tagline">Have fun playing with the trader</content>
<content tag="herounit">
    <div class="hero-unit">
        <h1>The trader</h1>

        <p>Welcome to the proof of concept of Axon Trader. This sample is created to showcase axon capabilities. Next to
            that we wanted to create a cool app with a nice front-end that we can really use as a showcase.</p>

        <p>If you are logged in, you can go to your dashboard.</p>

        <p><a class="btn primary large" href="${ctx}/dashboard">Dashboard &raquo;</a></p>
    </div>
</content>

<p>There are a few things implemented. You can choose the company to trade stock items for. Before you can
    use them you need to login.</p>

<div class="row">
    <div class="span5">
        <h2>Available Credentials</h2>
        <table class="zebra-striped">
            <thead>
            <tr>
                <th>User</th>
                <th>Password</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>buyer1</td>
                <td>buyer1</td>
            </tr>
            <tr>
                <td>buyer2</td>
                <td>buyer2</td>
            </tr>
            <tr>
                <td>buyer3</td>
                <td>buyer3</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="span4">
        <h2>Check the stocks</h2>

        <p>If you have logged in, you can go to the companies</p>

        <p><a class="btn primary" href="${ctx}/company">To the items &raquo;</a></p>
    </div>
    <div class="span5">
        <h2>Administration</h2>

        <p>We have a few options for creating new companies and new users. This can be found in the admin
            part of the website.</p>
    </div>
</div>

</body>
</html>