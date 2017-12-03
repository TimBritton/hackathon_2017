$(function () {
    var self = this;
    var updateIcons = function (map, sensors) {
        var iconBase = 'https://maps.google.com/mapfiles/kml/shapes/';
        var icons = {
            snowflake: {
                icon: iconBase + 'snowflake_simple.png'
            },
            sunny: {
                icon: iconBase + 'sunny.png'
            }
        };

        var features = [
            {
                position: new google.maps.LatLng(41.8963241, -87.65200379999999),
                type: 'snowflake'
            }, {
                position: new google.maps.LatLng(41.89631420000001, -87.6526083),
                type: 'snowflake'
            }, {
                position: new google.maps.LatLng(41.8946913, -87.65300230000003),
                type: 'snowflake'
            }, {
                position: new google.maps.LatLng(41.8911906, -87.64761729999998),
                type: 'sunny'
            }, {
                position: new google.maps.LatLng(41.8903461, -87.6340424),
                type: 'sunny'
            }
        ];

        var table = document.getElementById("detailTable");
        // Create an empty <tr> element and add it to the 1st position of the table:
        var row;

        var tableRows = table.getElementsByTagName('tr');
        var rowCount = tableRows.length;

        for (var x = rowCount - 1; x > 0; x--) {
            table.removeChild(tableRows[x]);
        }

        for (var i = 0; i < features.length; i++) {
            row = table.insertRow(i + 1);
            row.insertCell(0).innerHTML = "" + i;
            row.insertCell(1).innerHTML = features[i].position.lat();
            row.insertCell(2).innerHTML = features[i].position.lng();
            row.insertCell(3).innerHTML = (features[i].type == 'sunny') ? "NO" : "YES";
        }

        // Create markers.
        features.forEach(function (feature) {
            var marker = new google.maps.Marker({
                position: feature.position,
                icon: icons[feature.type].icon,
                map: map
            });
        });
    };

    var getSensors = function (params) {
        console.log(params);
        var url = "http://69.25.149.102:8080/api/v1/status/point?lon=" +
            params[0] + "&lat=" + params[1] + "&rad=" +
            params[2];
        console.log(url);
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", url, true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send(null);
        console.log(xhttp.responseText);
    };

    var getIcePath = function (params) {
        // var xhttp = new XMLHttpRequest();
        // xhttp.open("GET", "/api/v1/status/route", true);
        // xhttp.setRequestHeader("Content-type", "application/json");
        // xhttp.send(params);
        // return JSON.parse(xhttp.responseText);
    }

    radiusChange = function (radius) {
        document.getElementById(radius + 'value').innerHTML =
            document.getElementById(radius).value + ' miles';
    }

    /* Donut dashboard chart */
    Morris.Donut({
        element: 'dashboard-donut-1',
        data: [
            {label: "Solved", value: 2513},
            {label: "New", value: 10},
            {label: "On Progress", value: 311}
        ],
        colors: ['#33414E', '#1caf9a', '#FEA223'],
        resize: true
    });
    /* END Donut dashboard chart */

    $(".x-navigation-minimize").on("click", function () {
        setTimeout(function () {
            rdc_resize();
        }, 200);
    });

    if ($("#google_world_map").length > 0) {

        var gWorldCords = new google.maps.LatLng(41.895562, -87.65097);
        var gWorldOptions = {zoom: 15, center: gWorldCords, mapTypeId: google.maps.MapTypeId.ROADMAP}
        var gWorld = new google.maps.Map(document.getElementById("google_world_map"), gWorldOptions);
        initMap(gWorld);
        updateIcons(gWorld, []);
        gWorld.addListener('dragend', function () {
            getSensors([gWorld.getCenter().lng(), gWorld.getCenter().lat(),
                document.getElementById('radius2').value * 100]);
            updateIcons(gWorld, []);
            console.log(JSON.stringify({
                lon: gWorld.getCenter().lng(), lat: gWorld.getCenter().lat(),
                rad: document.getElementById('radius2').value
            }));
        });
    }

    if ($("#google_map_route").length > 0) {

        var gWorldCords = new google.maps.LatLng(41.895562, -87.65097);
        var gWorldOptions = {zoom: 15, center: gWorldCords, mapTypeId: google.maps.MapTypeId.ROADMAP};
        var gWorld1 = new google.maps.Map(document.getElementById("google_map_route"), gWorldOptions);
        initMap(gWorld1);
        new AutocompleteDirectionsHandler(gWorld1);
        updateIcons(gWorld1, []);
        gWorld1.addListener('dragend', function () {
            updateIcons(gWorld1, []);
            // getIcePath(JSON.stringify({location: [gWorld1.getCenter().lng(), gWorld1.getCenter().lat()], maxDistance: document.getElementById('radius2').value}));
            console.log(JSON.stringify({
                lon: gWorld1.getCenter().lng(), lat: gWorld1.getCenter().lat(),
                rad: document.getElementById('radius2').value
            }));
        });
    }

    var infoWindow;

    function initMap(map) {
        infoWindow = new google.maps.InfoWindow;

        // Try HTML5 geolocation.
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function (position) {
                var pos = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude
                };
                map.setCenter(pos);
            }, function () {
                handleLocationError(true, infoWindow, map.getCenter());
            });
        } else {
            // Browser doesn't support Geolocation
            handleLocationError(false, infoWindow, map.getCenter());
        }

    }

    function handleLocationError(browserHasGeolocation, infoWindow, pos) {
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
            'Error: The Geolocation service failed.' :
            'Error: Your browser doesn\'t support geolocation.');
        infoWindow.open(map);
    }

    function setupPlaceChangedListener(autocomplete, mode) {
        autocomplete.bindTo('bounds', gWorld1);
        autocomplete.addListener('place_changed', function () {
            var place = autocomplete.getPlace();
            if (!place.place_id) {
                window.alert("Please select an option from the dropdown list.");
                return;
            }
            if (mode === 'ORIG') {
                self.originPlaceId = place.place_id;
            } else {
                self.destinationPlaceId = place.place_id;
            }
            route();
        });
    };

    function route() {
        if (!self.originPlaceId || !self.destinationPlaceId) {
            return;
        }

        self.directionsService.route({
            origin: {'placeId': self.originPlaceId},
            destination: {'placeId': self.destinationPlaceId},
            travelMode: 'DRIVING'
        }, function (response, status) {
            if (status === 'OK') {
                getIcePath(response);
                var array = [];
                var point;
                for (var i = 0; i < response.routes[0].legs[0].steps.length; i++) {
                    point = response.routes[0].legs[0].steps[i];
                    if (i < response.routes[0].legs[0].steps.length - 1) {
                        array.push([point.start_location.lng(), point.start_location.lat()]);
                    } else {
                        array.push([point.end_location.lng(), point.end_location.lat()]);
                    }
                }
                console.log(array);
                self.directionsDisplay.setDirections(response);
            } else {
                window.alert('Directions request failed due to ' + status);
            }
        });
    };

    function AutocompleteDirectionsHandler(map) {
        self.originPlaceId = null;
        self.destinationPlaceId = null;
        self.originInput = document.getElementById('origin-input');
        self.destinationInput = document.getElementById('destination-input');
        self.modeSelector = this.travelMode;
        self.directionsService = new google.maps.DirectionsService;
        self.directionsDisplay = new google.maps.DirectionsRenderer;
        self.directionsDisplay.setMap(map);

        var originAutocomplete = new google.maps.places.Autocomplete(
            self.originInput, {placeIdOnly: true});
        var destinationAutocomplete = new google.maps.places.Autocomplete(
            self.destinationInput, {placeIdOnly: true});

        setupPlaceChangedListener(originAutocomplete, 'ORIG');
        setupPlaceChangedListener(destinationAutocomplete, 'DEST');

        map.controls[google.maps.ControlPosition.TOP_LEFT].push(self.originInput);
        map.controls[google.maps.ControlPosition.TOP_LEFT].push(self.destinationInput);
        map.controls[google.maps.ControlPosition.TOP_LEFT].push(self.modeSelector);
    }

});

