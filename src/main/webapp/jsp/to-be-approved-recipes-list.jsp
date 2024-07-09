<%@ page contentType="text/html;charset=utf-8" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<!DOCTYPE html>

		<html lang="en">

		<c:import url="/html/include/header.html" />

		<body class="bg-2">
			<c:import url="/jsp/include/log-banner.jsp" />

			<main>
				<div class="flex-grow w-full pt-24 px-6 flex flex-col items-center">
					<h1 class="ranked-title text-4xl font-medium" style="font-family: Georgia, serif;">Pending Recipes
					</h1>
					<hr class="w-full border-t border-brown-300 my-6" />

					<div id="contextMenu" class="context-menu" style="display:none">
						<ul>
							<li>
								<div>
									<button type="submit" id="cmApproveButton" class="cmButton1">
										<img src="media/img/approve.png" width="20px" height="20px">
										Approve
									</button>
								</div>
							</li>
							<li>
								<div>
									<button type="submit" id="cmRejectButton" class="cmButton1">
										<img src="media/img/reject.png" width="20px" height="20px">
										Reject
									</button>
								</div>
							</li>
						</ul>
					</div>

					<!-- display the list of found recipes, if any -->
					<c:choose>
						<c:when test='${not empty recipesToBeApprovedList}'>
							<p id="hiddentxt" style="display:none">No recipe to be approved found in the database.</br>
							</p>
							<div id="removable" class="container rounded-lg mx-auto px-4 overflow-x-auto">
								<div class="pb-2">
									<button type="submit" id="approveButton"
										class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 px-3 mt-4">Approve</button>
									<span> </span>
									<button type="submit" id="rejectButton"
										class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 px-3 mt-4">Reject</button>
								</div>
								<div class="inline-block min-w-full shadow rounded-lg overflow-hidden">
									<table id="myTable" class="min-w-full leading-normal">
										<thead>
											<tr>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													<input type="checkbox" id="check-all"><label for="check-all"> Check
														All!</label>
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Id
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Name
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Creation Date
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Creator Id
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Status
												</th>
											</tr>
										</thead>

										<tbody>
											<c:forEach var="recipe" items="${recipesToBeApprovedList}">
												<tr>
													<th scope="row"
														class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<input type="checkbox">
													</th>
													<td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<c:out value="${recipe.id}" />
													</td>
													<td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<a href="recipeDetails?recipeID=${recipe.id}">
															<c:out value="${recipe.name}" />
														</a>
													</td>
													<td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<c:out value="${recipe.getCreationDate()}" />
													</td>
													<td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<c:out value="${recipe.user_id}" />
													</td>
													<td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<c:choose>
															<c:when test="${recipe.approved == null}">
																<span
																	class="relative inline-block px-3 py-1 font-semibold text-orange-900 leading-tight">
																	<span aria-hidden
																		class="absolute inset-0 bg-orange-200 opacity-50 rounded-full"></span>
																	<span class="relative">To be approved</span>
																</span>
															</c:when>
															<c:when test="${recipe.approved == true}">
																<span
																	class="relative inline-block px-3 py-1 font-semibold text-green-900 leading-tight">
																	<span aria-hidden
																		class="absolute inset-0 bg-green-200 opacity-50 rounded-full"></span>
																	<span class="relative">Approved</span>
																</span>
															</c:when>
															<c:when test="${recipe.approved == false}">
																<span
																	class="relative inline-block px-3 py-1 font-semibold text-red-900 leading-tight">
																	<span aria-hidden
																		class="absolute inset-0 bg-red-200 opacity-50 rounded-full"></span>
																	<span class="relative">Rejected</span>
																</span>
															</c:when>
														</c:choose>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<p>No recipe to be approved found in the database.</br></p>
						</c:otherwise>
					</c:choose>
					<div class="px-4">
						<a href="usr_control"><button type="submit"
								class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 px-3 mt-4">Go
								Back</button></a>
						<span> </span>
						<a href="home"><button type="submit"
								class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 px-3 mt-4">Go
								Home</button></a>
					</div>
				</div>
			</main>
			<br>
			<br>

			<c:import url="/html/include/footer.html" />
		</body>

		<script src="<c:url value='/js/approve-reject-recipes.js'/>"></script>

		</html>