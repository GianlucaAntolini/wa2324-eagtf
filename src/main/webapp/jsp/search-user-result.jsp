<%@ page contentType="text/html;charset=utf-8" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

		<!DOCTYPE html>
		<html lang="en">

		<c:import url="/html/include/header.html" />

		<body class="bg-2">
			<c:import url="/jsp/include/log-banner.jsp" />

			<main>
				<div class="flex-grow w-full pt-24 px-6 flex flex-col items-center">
					<h1 class="ranked-title text-4xl font-medium" style="font-family: Georgia, serif;">Users Found</h1>
					<hr class="w-full border-t border-brown-300 my-6" />

					<!-- display the list of found employees, if any -->
					<c:choose>
						<c:when test='${not empty userList}'>
							<div class="container rounded-lg mx-auto px-4 overflow-x-auto">
								<div class="inline-block min-w-full shadow rounded-lg overflow-hidden">
									<table id="myTable" class="min-w-full leading-normal">
										<thead>
											<tr>
												<th id="myTable"
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Id
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Email
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Registration_date
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Name
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Surname
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													Role
												</th>
												<th
													class="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
													(Un)Ban
												</th>
											</tr>
										</thead>

										<tbody>
											<c:forEach var="user" items="${userList}">
												<tr>
													<th scope="row"
														class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<c:out value="${user.id}" />
													</th>
													<td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<c:out value="${user.email}" />
													</td>
													<td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<c:out value="${user.registration_date}" />
													</td>
													<td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<c:out value="${user.name}" />
													</td>
													<td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<c:out value="${user.surname}" />
													</td>
													<td class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
														<c:choose>
															<c:when test="${user.role_id == 0}">
																<span
																	class="relative inline-block px-3 py-1 font-semibold text-green-900 leading-tight text-center w-20">
																	<span aria-hidden
																		class="absolute inset-0 bg-green-200 opacity-50 rounded-full"></span>
																	<span class="relative">Admin</span>
																</span>
															</c:when>
															<c:when test="${user.role_id == 1}">
																<span
																	class="relative inline-block px-3 py-1 font-semibold text-yellow-900 leading-tight text-center w-20">
																	<span aria-hidden
																		class="absolute inset-0 bg-yellow-200 opacity-50 rounded-full"></span>
																	<span class="relative">User</span>
																</span>
															</c:when>
															<c:when test="${user.role_id == 2}">
																<span
																	class="relative inline-block px-3 py-1 font-semibold text-red-900 leading-tight text-center w-20">
																	<span aria-hidden
																		class="absolute inset-0 bg-red-200 opacity-50 rounded-full"></span>
																	<span class="relative">Banned</span>
																</span>
															</c:when>
														</c:choose>
													</td>
													<c:choose>
														<c:when test="${user.role_id > 0}">
															<td
																class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
																<button type="submit"
																	class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 w-24 px-3">
																	<c:choose>
																		<c:when test="${user.role_id == 2}">
																			Unban
																		</c:when>
																		<c:otherwise>
																			Ban
																		</c:otherwise>
																	</c:choose>
																</button>
															</td>
														</c:when>
														<c:otherwise>
															<td
																class="px-5 py-5 border-b border-gray-200 bg-white text-sm">
																<button type="submit" disabled
																	class="bg-neutral-400 text-white text-sm px-4 rounded-lg h-8 w-24 px-3">
																	Ban
																</button>
															</td>
														</c:otherwise>
													</c:choose>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<p>No user with email
								<c:out value="${email}" /> found in the database.
							</p>
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

		<script src="<c:url value='/js/user-ban.js'/>"></script>

		</html>