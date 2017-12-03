/////////////////////////////////////////////////////////////////////////////////
// Copyright (c) Autodesk, Inc. All rights reserved
// Written by Jaime Rosales 2016 - Forge Developer Partner Services
//
// Permission to use, copy, modify, and distribute this software in
// object code form for any purpose and without fee is hereby granted,
// provided that the above copyright notice appears in all copies and
// that both that copyright notice and the limited warranty and
// restricted rights notice below appear in all supporting
// documentation.
//
// AUTODESK PROVIDES THIS PROGRAM "AS IS" AND WITH ALL FAULTS.
// AUTODESK SPECIFICALLY DISCLAIMS ANY IMPLIED WARRANTY OF
// MERCHANTABILITY OR FITNESS FOR A PARTICULAR USE.  AUTODESK, INC.
// DOES NOT WARRANT THAT THE OPERATION OF THE PROGRAM WILL BE
// UNINTERRUPTED OR ERROR FREE.
/////////////////////////////////////////////////////////////////////////////////
//dXJuOmFkc2sub2JqZWN0czpvcy5vYmplY3Q6cmVhY3QtbGF5b3V0LXNhbXBsZS12aWV3ZXIzN2lleGRiZG5xY2Franplc2JocWNlcTVhc2htNzh2My9VcmJhbkhvdXNlLTIwMTUucnZ0

var viewer;
var options = {
    env: 'AutodeskProduction',
    getAccessToken: getForgeToken
}

var documentId = 'urn:dXJuOmFkc2sub2JqZWN0czpvcy5vYmplY3Q6ZnJvc3R5ZnNha2J5OTFwZnpxbWt1dWdzdHhhb25mcXQwc2V5Y3IvYnJpZGdlLnJ2dA';
var initState ={"seedURN":"dXJuOmFkc2sub2JqZWN0czpvcy5vYmplY3Q6ZnJvc3R5ZnNha2J5OTFwZnpxbWt1dWdzdHhhb25mcXQwc2V5Y3IvYnJpZGdlLnJ2dA","objectSet":[{"id":[],"isolated":[],"hidden":[3867],"explodeScale":0,"idType":"lmv"}],"viewport":{"name":"","eye":[170.1368454511809,-166.3783048719518,174.59027838803698],"target":[-1.9005090222146077,-0.4045681475562721,1.4869713197386796],"up":[-0.42209583254734806,0.40721866944607044,0.8099432470245882],"worldUpVector":[0,0,1],"pivotPoint":[-9.55905594598326,-4.319267912224376,5.344891859153478],"distanceToOrbit":295.1421480142159,"aspectRatio":1.2647058823529411,"projection":"orthographic","isOrthographic":true,"orthographicHeight":295.1421480142158},"renderOptions":{"environment":"Sharp Highlights","ambientOcclusion":{"enabled":true,"radius":10,"intensity":0.4},"toneMap":{"method":1,"exposure":-9,"lightMultiplier":-1e-20},"appearance":{"ghostHidden":true,"ambientShadow":true,"antiAliasing":true,"progressiveDisplay":true,"swapBlackAndWhite":false,"displayLines":true,"displayPoints":true}},"cutplanes":[]}


Autodesk.Viewing.Initializer(options, function onInitialized() {
    Autodesk.Viewing.Document.load(documentId, onDocumentLoadSuccess, onDocumentLoadFailure);
});

/**
 * Autodesk.Viewing.Document.load() success callback.
 * Proceeds with model initialization.
 */
function onDocumentLoadSuccess(doc) {

    // A document contains references to 3D and 2D viewables.
    var viewable = Autodesk.Viewing.Document.getSubItemsWithProperties(doc.getRootItem(), {
        'type': 'geometry',
        'role': '3d'
    }, true);
    if (viewable.length === 0) {
        console.error('Document contains no viewables.');
        return;
    }

    // Choose any of the available viewable
    var initialViewable = viewable[0]; // You can check for other available views in your model,
    var svfUrl = doc.getViewablePath(initialViewable);
    var modelOptions = {
        sharedPropertyDbPath: doc.getPropertyDbPath()
    };

    var viewerDiv = document.getElementById('viewerDiv');

    ///////////////USE ONLY ONE OPTION AT A TIME/////////////////////////

    /////////////////////// Headless Viewer /////////////////////////////
    // viewer = new Autodesk.Viewing.Viewer3D(viewerDiv);
    //////////////////////////////////////////////////////////////////////
    //////////////////Viewer with Autodesk Toolbar///////////////////////
    viewer = new Autodesk.Viewing.Private.GuiViewer3D(viewerDiv);
    //////////////////////////////////////////////////////////////////////
    viewer.addEventListener(Autodesk.Viewing.GEOMETRY_LOADED_EVENT, onGeometryLoaded)

    viewer.start(svfUrl, modelOptions, onLoadModelSuccess, onLoadModelError);
}

function onGeometryLoaded(event) {
  viewer.restoreState(initState);
  $('#snow-node').css('visibility', 'visible');
  $('#snow-node2').css('visibility', 'visible');
}

/**
 * Autodesk.Viewing.Document.load() failure callback.
 */
function onDocumentLoadFailure(viewerErrorCode) {
    console.error('onDocumentLoadFailure() - errorCode:' + viewerErrorCode);
}

/**
 * viewer.loadModel() success callback.
 * Invoked after the model's SVF has been initially loaded.
 * It may trigger before any geometry has been downloaded and displayed on-screen.
 */
function onLoadModelSuccess(model) {
    console.log('onLoadModelSuccess()!');
    console.log('Validate model loaded: ' + (viewer.model === model));
    console.log(model);
}

/**
 * viewer.loadModel() failure callback.
 * Invoked when there's an error fetching the SVF file.
 */
function onLoadModelError(viewerErrorCode) {
    console.error('onLoadModelError() - errorCode:' + viewerErrorCode);
}


/////////////////////////////////////////////////////////////////////////////////
//
// Load Viewer Background Color Extension
//
/////////////////////////////////////////////////////////////////////////////////

function changeBackground (){
       viewer.setBackgroundColor(0, 59, 111, 255,255, 255);
}

/////////////////////////////////////////////////////////////////////////////////
//
// Unload Viewer Background Color Extension
//
/////////////////////////////////////////////////////////////////////////////////

function resetBackground (){
       viewer.setBackgroundColor(169,169,169, 255,255, 255);
}

/////////////////////////////////////////////////////////////////////////////////
//
// Load Viewer Markup3D Extension
//
/////////////////////////////////////////////////////////////////////////////////
// 3D Markup extension to display values of the selected objects in the model.

function loadMarkup3D (){
       viewer.loadExtension('Viewing.Extension.Markup3D');
}

/////////////////////////////////////////////////////////////////////////////////
//
// Load Viewer Transform Extension
//
/////////////////////////////////////////////////////////////////////////////////
// Transformation is allowed with this extension to move object selected in the XYZ
// position or rotation in XYZ as well.

function loadTransform (){
       viewer.loadExtension('Viewing.Extension.Transform');
}

/////////////////////////////////////////////////////////////////////////////////
//
// Load Viewer Control Selector Extension
//
/////////////////////////////////////////////////////////////////////////////////
// This extension allows you to remove certain extensions from the original toolbar
// provided to you.

function loadControlSelector(){
       viewer.loadExtension('_Viewing.Extension.ControlSelector');
}
