app.factory('util', function() {

	/* Возвращает объект по objectId */
	function getObjectInArrayById(arr, id) {
		for (var i = 0; i < arr.length; i++) {
			if (arr[i].objectId === id) {
				return arr[i];
			}
		};
	}

	/* Для листания страниц с объектами */
	function nextEntities(arrLength, startPaging, objectsOnPage) {
		if (arrLength > startPaging + objectsOnPage) {
			return startPaging + objectsOnPage;
		} else return startPaging;
	}
	function previousEntities(startPaging, objectsOnPage) {
		if (startPaging - objectsOnPage >= 0) {
			return startPaging - objectsOnPage;
		} else return 0;
	}



	return {
		getObjectInArrayById: getObjectInArrayById,
		nextEntities: nextEntities,
		previousEntities: previousEntities
	}
});