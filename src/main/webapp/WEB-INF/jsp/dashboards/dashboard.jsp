<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="dashboard">
<c:if test="${meetingDashboard.numberOfMeetings==0}">
<div style="width: 100%;text-align: center;">
	<h2 id="emptyDashboard"><font size="10">There are no meetings last month</font></h2>
</div>
</c:if>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
<div style="height: 500px;width: 100%;background: white">
<div id="chart1" style="height: 500px; width: 37%;display: inline-block;margin-top: 30px">
<canvas id="pie-chart"></canvas>
</div>

<div id="chart2" style="height: 500px; width: 45%;display: inline-block;margin-left: 150px;margin-top: 30px">
<canvas id="bar-chart" height=250></canvas>
</div>
</div>
<script type="text/javascript">
new Chart(document.getElementById("bar-chart"), {
    type: 'bar',
    data: {
      labels: [
    		<c:forEach var ="i" items="${meetingDashboard.meetingsByDay}" varStatus="status">
			"<c:out value='${i[0]}'/>",
			</c:forEach>],
      datasets: [
        {
          label: "Number of meetings",
          backgroundColor: [
        	  'rgba(255, 99, 132, 0.6)',
              'rgba(54, 162, 235, 0.6)',
              'rgba(255, 206, 86, 0.6)',
              'rgba(75, 192, 192, 0.6)',
              'rgba(153, 102, 255, 0.6)',
              'rgba(255, 159, 64, 0.6)',
              'rgba(255, 99, 132, 0.6)',
              'rgba(54, 162, 235, 0.6)',
              'rgba(255, 206, 86, 0.6)',
              'rgba(75, 192, 192, 0.6)',
              'rgba(153, 102, 255, 0.6)',
              'rgba(54, 162, 235, 0.6)',
              'rgba(255, 206, 86, 0.6)',
              'rgba(75, 192, 192, 0.6)',
              'rgba(153, 102, 255, 0.6)',
              'rgba(255, 159, 64, 0.6)',
              'rgba(255, 99, 132, 0.6)',
              'rgba(54, 162, 235, 0.6)',
              'rgba(255, 206, 86, 0.6)',
              'rgba(75, 192, 192, 0.6)',
              'rgba(153, 102, 255, 0.6)',
              'rgba(54, 162, 235, 0.6)',
              'rgba(255, 206, 86, 0.6)',
              'rgba(75, 192, 192, 0.6)',
              'rgba(153, 102, 255, 0.6)',
              'rgba(255, 159, 64, 0.6)',
              'rgba(255, 99, 132, 0.6)',
              'rgba(54, 162, 235, 0.6)',
              'rgba(255, 206, 86, 0.6)',
              'rgba(75, 192, 192, 0.6)',
              'rgba(153, 102, 255, 0.6)'
          ],
          data: [
        		<c:forEach var ="i" items="${meetingDashboard.meetingsByDay}" varStatus="status">
        		<c:out value='${i[1]}'/> ,
    			</c:forEach>
        		
        	  ]
        }
      ]
    },
    options: {
      legend: { display: true },
      title: {
        display: true,
        text: 'Number of meetings by day last month'
      }
    }
});

new Chart(document.getElementById("pie-chart"), {
    type: 'pie',
    data: {
      labels: [
    	  <c:forEach var ="i" items="${meetingDashboard.assistantByGenre}" varStatus="status">
    	  "<c:out value='${i[0]}'/>",
			</c:forEach>
      ],
      datasets: [{
        label: "Assistants",
        backgroundColor: [
        	'rgba(255, 99, 132, 0.6)',
            'rgba(54, 162, 235, 0.6)',
            'rgba(255, 206, 86, 0.6)',
            'rgba(75, 192, 192, 0.6)',
            'rgba(153, 102, 255, 0.6)',
            'rgba(255, 159, 64, 0.6)',
            'rgba(255, 99, 132, 0.6)',
            'rgba(54, 162, 235, 0.6)',
            'rgba(255, 206, 86, 0.6)',
            'rgba(75, 192, 192, 0.6)',
            'rgba(153, 102, 255, 0.6)',
            'rgba(54, 162, 235, 0.6)',
            'rgba(255, 206, 86, 0.6)',
            'rgba(75, 192, 192, 0.6)',
            'rgba(153, 102, 255, 0.6)',
            'rgba(255, 159, 64, 0.6)',
            'rgba(255, 99, 132, 0.6)',
            'rgba(54, 162, 235, 0.6)',
            'rgba(255, 206, 86, 0.6)',
            'rgba(75, 192, 192, 0.6)',
            'rgba(153, 102, 255, 0.6)',
            'rgba(54, 162, 235, 0.6)',
            'rgba(255, 206, 86, 0.6)',
            'rgba(75, 192, 192, 0.6)'
		],
        data: [
        	<c:forEach var ="i" items="${meetingDashboard.assistantByGenre}" varStatus="status">
			<c:out value='${i[1]}'/>,
			</c:forEach>
        ]
      }]
    },
    options: {
      title: {
        display: true,
        text: 'Assistant by genre last month'
      }
    }
});
</script>
<div style="width: 100%;background: white;vertical-align: top">
<div style="width: 70%;display: inline-block;vertical-align:top;margin-left: 30px;">
 <table id="assistantByMeetings" class="table table-striped" style="background: white">
        <thead>
        <tr>
            <th style="width: 300px;">Meeting</th>
            <th style="width: 200px;">Book</th>
         	<th style="width: 200px;">Place</th>
         	<th style="width: 20px;">Day</th>
            <th style="width: 30px">Assistants</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${meetingDashboard.assistantByMeeting}" var="meet">
            <tr>
                <td>
                   <c:out value="${meet[0]}"/>
                </td>
                <td>
                    <c:out value="${meet[1]}"/>
                </td>
                <td>
                    <c:out value="${meet[2]}"/>
                </td>
                <td>
                    <c:out value="${meet[3]}"/>
                </td>        
                <td align="center">
                    <c:out value="${meet[4]}"/>
                </td>      
            </tr>
        </c:forEach>
        </tbody>
    </table>
   </div>
<div style="width: 20%;display: inline-block;text-align: center;align-content: center;margin-left: 60px;">
<div style="height: 100px;border: 2px solid black;border-radius: 10px;box-shadow: 3px 3px 0px black;">
<br>
<h2><c:out value="${meetingDashboard.numberOfMeetings}"/></h2>
<p>Number of meetings last month</p>
</div>
<br>
<div style="height: 100px;border: 2px solid black;border-radius: 10px;box-shadow: 3px 3px 0px black;">
<br>
<h2><c:out value="${meetingDashboard.numberOfMeetingsAssistant}"/></h2>
<p>Number of assistant last month</p>
</div>
<br>
<div style="height: 100px;border: 2px solid black;border-radius: 10px;box-shadow: 3px 3px 0px black;">
<br>
<h2><c:out value="${meetingDashboard.usersAssisted}"/>%</h2>
<p>Users who have assisted a meeting last month</p>
</div>
<br><br><br>
</div>
   </div>
    </petclinic:layout>