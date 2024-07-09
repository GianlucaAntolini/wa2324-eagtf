<%@ page contentType="text/html;charset=utf-8" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<style>
			html {
				overflow-y: scroll;
			}

			table,
			th,
			td {
				border: 1px solid black;
			}
		</style>
		<c:import url="/jsp/include/show-message.jsp" />

		<!-- display the list of found recipes, if any -->
		<c:choose>
			<c:when test='${not empty recipesList}'>
				<table>
					<h1>${name_table}</h1>
					<thead class="bg-3">
						<tr>
							<th>Id</th>
							<th>name</th>
							<th>Likes</th>
						</tr>
					</thead>

					<tbody class="bg-4">
						<c:forEach var="recipe" items="${recipesList}">
							<tr>
								<th scope="row">
									<c:out value="${recipe.id}" />
								</th>
								<td><a href="recipeDetails?recipeID=${recipe.id}">
										<c:out value="${recipe.name}" />
									</a></td>
								<td>${recipe.getNLikes()}</td>
							</tr>
						</c:forEach>
					</tbody>
			</c:when>
			<c:otherwise>
				${empty_table_message}
			</c:otherwise>
		</c:choose>