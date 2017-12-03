$(function () {
    var self = this;
    var updateIcons = function (map) {
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
                position: new google.maps.LatLng(-33.91721, 151.22630),
                type: 'snowflake'
            }, {
                position: new google.maps.LatLng(-33.91539, 151.22820),
                type: 'snowflake'
            }, {
                position: new google.maps.LatLng(-33.91747, 151.22912),
                type: 'snowflake'
            }, {
                position: new google.maps.LatLng(-33.91910, 151.22907),
                type: 'snowflake'
            }, {
                position: new google.maps.LatLng(-33.91725, 151.23011),
                type: 'snowflake'
            }, {
                position: new google.maps.LatLng(-33.91872, 151.23089),
                type: 'snowflake'
            }, {
                position: new google.maps.LatLng(-33.91784, 151.23094),
                type: 'sunny'
            }, {
                position: new google.maps.LatLng(-33.91682, 151.23149),
                type: 'sunny'
            }, {
                position: new google.maps.LatLng(-33.91790, 151.23463),
                type: 'sunny'
            }
        ];

        // Create markers.
        features.forEach(function (feature) {
            var marker = new google.maps.Marker({
                position: feature.position,
                icon: icons[feature.type].icon,
                map: map
            });
        });
    };

    var getSensors = function () {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "/api/v1/status/point", true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send();
        return JSON.parse(xhttp.responseText);
    };

    var getIcePath = function () {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "/api/v1/status/route", true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send();
        return JSON.parse(xhttp.responseText);
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
        gWorld.addListener('dragend', function () {
            updateIcons(gWorld);
            // 3 seconds after the center of the map has changed, pan back to the
            // marker.
            // getSensors(JSON.stringify({location: [gWorld.getCenter().lng(), gWorld.getCenter().lat()], maxDistance: 100}));
            console.log(JSON.stringify({
                location: [gWorld.getCenter().lng(), gWorld.getCenter().lat()],
                maxDistance: document.getElementById('radius1').value
            }));
        });
    }

    if ($("#google_map_route").length > 0) {

        var gWorldCords = new google.maps.LatLng(41.895562, -87.65097);
        var gWorldOptions = {zoom: 15, center: gWorldCords, mapTypeId: google.maps.MapTypeId.ROADMAP};
        var gWorld1 = new google.maps.Map(document.getElementById("google_map_route"), gWorldOptions);
        initMap(gWorld1);
        new AutocompleteDirectionsHandler(gWorld1);

        gWorld1.addListener('dragend', function () {
            updateIcons(gWorld1);
            // 3 seconds after the center of the map has changed, pan back to the
            // marker.
            // getSensors(JSON.stringify({location: [gWorld.getCenter().lng(), gWorld.getCenter().lat()], maxDistance: 100}));
            console.log(JSON.stringify({
                location: [gWorld.getCenter().lng(), gWorld.getCenter().lat()],
                maxDistance: document.getElementById('radius1').value
            }));
        });
    }

    updateRoute = function () {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", `https://maps.googleapis.com/maps/api/directions/json?origin=${document.getElementById('origin-input').value}&destination=${document.getElementById('destination-input').value}l&key=AIzaSyCefiZQI-ja0UjzlJvr15Y4zkrZtIrrgkY`, true);
        xhttp.send();
        return JSON.parse(xhttp.responseText);
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

