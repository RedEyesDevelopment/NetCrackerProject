'use strict';

var model = {
    roomTypes: [
        {
            title: 'Economy',
            image: 'img/hotel_luxe.jpg',
            content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla hendrerit arcu in quam ullamcorper, ut mollis urna tincidunt. Phasellus posuere.'
        },

        {
            title: 'SemiLuxe',
            image: 'img/hotel_luxe.jpg',
            content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla hendrerit arcu in quam ullamcorper, ut mollis urna tincidunt. Phasellus posuere.'
        },

        {
            title: 'Luxe',
            image: 'img/hotel_luxe.jpg',
            content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla hendrerit arcu in quam ullamcorper, ut mollis urna tincidunt. Phasellus posuere.'
        },

        {
            title: 'President',
            image: 'img/hotel_luxe.jpg',
            content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla hendrerit arcu in quam ullamcorper, ut mollis urna tincidunt. Phasellus posuere.'
        }
    ]
    };

var hotelApp = angular.module("hotelApp", []);
hotelApp.controller("roomTypeController", function ($scope) {
    $scope.list = model;
});