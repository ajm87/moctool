(function() {
    'use strict';
    
    angular
        .module('moctoolApp')
        .service('CytoscapeService', CytoscapeService);

        CytoscapeService.$inject = ['$compile'];

        function CytoscapeService($compile) {
            var cy;
            init();
            function init() {
                    cy = cytoscape({
                    container: $('#cyCanvas'),
                    layout: 'dagre',
                    style: [
                        {
                            selector: 'edge',
                            style: {
                                'width': 1,
                                'line-color': 'black',
                                'curve-style': 'bezier',
                                'target-arrow-color': 'black',
                                'target-arrow-shape': 'triangle',
                                'label': 'data(label)',
                                'text-margin-y': '-10px'
                            }
                        },
                        {
                            selector: 'node',
                            style: {
                                'width': '50px',
                                'height': '50px',
                                'content': 'data(name)',
                                'text-valign': 'center',
                                'text-halign': 'center',
                                'background-color': '#fafafa',
                                'border-style': 'solid',
                                'border-width': '1px',
                                'border-color': 'black'
                            }
                        },
                        {
                            selector: '[accept = "true"]',
                            style: {
                                'border-style': 'double',
                                'border-width': '4px',
                                'border-color': 'black'
                            }
                        },
                        {
                            selector: '[initial = "true"]',
                            style: {
                                'background-image': 'content/images/Arrow_east.svg_.png',
                                'background-clip': 'node',
                                'background-fit': 'contain',
                                'background-position-x': '-29px',
                                
                            }
                        },
                        {
                            selector: '.hidden',
                            style: {
                                'width:': '0px',
                                'height': '0px',
                                'border-width': '0px'
                            }
                        }
                        ]
                });

                var defaults = {
        zoomFactor: 0.05, // zoom factor per zoom tick
        zoomDelay: 45, // how many ms between zoom ticks
        minZoom: 0.1, // min zoom level
        maxZoom: 10, // max zoom level
        fitPadding: 50, // padding when fitting
        panSpeed: 10, // how many ms in between pan ticks
        panDistance: 10, // max pan distance per tick
        panDragAreaSize: 75, // the length of the pan drag box in which the vector for panning is calculated (bigger = finer control of pan speed and direction)
        panMinPercentSpeed: 0.25, // the slowest speed we can pan by (as a percent of panSpeed)
        panInactiveArea: 8, // radius of inactive area in pan drag box
        panIndicatorMinOpacity: 0.5, // min opacity of pan indicator (the draggable nib); scales from this to 1.0
        zoomOnly: false, // a minimal version of the ui only with zooming (useful on systems with bad mousewheel resolution)
        fitSelector: undefined, // selector of elements to fit
        animateOnFit: function(){ // whether to animate on fit
            return false;
        },
        fitAnimationDuration: 1000, // duration of animation on fit

        // icon class names
        sliderHandleIcon: 'fa fa-minus',
        zoomInIcon: 'fa fa-plus',
        zoomOutIcon: 'fa fa-minus',
        resetIcon: 'fa fa-expand'
        };

        cy.panzoom( defaults );

        var autopanOptions = {
            enabled: true, // Whether the extension is enabled on register
            selector: 'node', // Which elements will be affected by this extension
            speed: 0.1 // Speed of panning when elements exceed canvas bounds
        };

        cy.autopanOnDrag(autopanOptions);

        var ctxOptions = {
            // List of initial menu items
            menuItems: [
            {
                id: 'initial', // ID of menu item
                title: 'Mark as initial', // Title of menu item
                // Filters the elements to have this menu item on cxttap
                // If the selector is not truthy no elements will have this menu item on cxttap
                selector: '[initial = "false"]', 
                onClickFunction: function (event) { // The function to be executed on click
                    event.cyTarget.data('initial', 'true');
                    event.cyTarget.addClass('initial');
                    event.cyTarget.removeClass('non-initial');

                },
                disabled: false, // Whether the item will be created as disabled
                coreAsWell: false // Whether core instance have this item on cxttap
            },
            {
                id: 'non-initial',
                title: 'Mark as non-initial state',
                selector: '[initial = "true"]',
                onClickFunction: function (event) {
                    event.cyTarget.data('initial', 'false');
                }
            },
            {
                id: 'accept',
                title: 'Mark as accept state',
                selector: '[accept = "false"]',
                onClickFunction: function (event) {
                    event.cyTarget.data('accept', 'true');
                },
            },
            {
                id: 'non-accept',
                title: 'Mark as non-accept state',
                selector: '[accept = "true"]',
                onClickFunction: function (event) {
                    event.cyTarget.data('accept', 'false');
                }
            },
            {
                id: 'remove',
                title: 'Remove state',
                selector: 'node',
                onClickFunction: function (event) {
                    event.cyTarget.remove();
                }
            },
            {
                id: 'remove-edge',
                title: 'Remove transition',
                selector: 'edge',
                onClickFunction: function(event) {
                    event.cyTarget.remove();
                }
            }
            ],
            // css classes that menu items will have
            menuItemClasses: [
            ],
            // css classes that context menu will have
            contextMenuClasses: [
            // add class names to this list
            ]
        };

        cy.contextMenus( ctxOptions );
        }

    this.getCytoscapeInstance = function($scope) {
        init();
        // the default values of each option are outlined below:
        var edgehandlesDefaults = {
        preview: true, // whether to show added edges preview before releasing selection
        stackOrder: 4, // Controls stack order of edgehandles canvas element by setting it's z-index
        handleSize: 10, // the size of the edge handle put on nodes
        handleColor: '#000000', // the colour of the handle and the line drawn from it
        handleLineType: 'ghost', // can be 'ghost' for real edge, 'straight' for a straight line, or 'draw' for a draw-as-you-go line
        handleLineWidth: 1, // width of handle line in pixels
        handleIcon: false, // Pass an Image-object to use as icon on handle. Icons are resized according to zoom and centered in handle.
        handleNodes: 'node', // selector/filter function for whether edges can be made from a given node
        hoverDelay: 50, // time spend over a target node before it is considered a target selection
        cxt: false, // whether cxt events trigger edgehandles (useful on touch)
        enabled: true, // whether to start the extension in the enabled state
        toggleOffOnLeave: true, // whether an edge is cancelled by leaving a node (true), or whether you need to go over again to cancel (false; allows multiple edges in one pass)
        edgeType: function( sourceNode, targetNode ) {
            // can return 'flat' for flat edges between nodes or 'node' for intermediate node between them
            // returning null/undefined means an edge can't be added between the two nodes
            return 'flat';
        },
        loopAllowed: function( node ) {
            // for the specified node, return whether edges from itself to itself are allowed
            return true;
        },
        nodeLoopOffset: -50, // offset for edgeType: 'node' loops
        nodeParams: function( sourceNode, targetNode ) {
            // for edges between the specified source and target
            // return element object to be passed to cy.add() for intermediary node
            return {};
        },
        edgeParams: function( sourceNode, targetNode, i ) {
            // for edges between the specified source and target
            // return element object to be passed to cy.add() for edge
            // NB: i indicates edge index in case of edgeType: 'node'
            return {};
        },
        start: function( sourceNode ) {
            // fired when edgehandles interaction starts (drag on handle)
        },
        complete: function( sourceNode, targetNodes, addedEntities ) {
            // fired when edgehandles is done and entities are added
            addedEntities.data('label', '\u03b5');
            addedEntities.qtip({
                content: {
                    text: function(api) {
                        var tipScope = $scope.$new();
                        tipScope.connector = addedEntities;
                        tipScope.symbol = addedEntities.data('label') === '\u03b5' ? null : addedEntities.data('label');
                        var ele = angular.element('<div class="form-group"><label>Input a symbol for this transition:<br><span style="font-size: 0.75em">(Leave blank for an &epsilon; transition)</span></label><input type="text" class="transition" ng-model="symbol" placeholder="&epsilon;" ng-change="vm.setSymbol(connector, symbol)"></div><div class="form-actions"><button type="button" class="btn btn-primary" ng-click="vm.hideQtip(connector)">OK</button></div>');
                        $compile(ele)(tipScope);
                        return ele;
                    }
                },
                show: {
                    event: 'click'
                },
                hide: {
                    event: 'unfocus'
                },
                position: {
                    my: 'top center',
                    at: 'bottom center'
                },
                style: {
                    classes: 'qtip-bootstrap qtip-shadow'
                },
                events: {
                    render: function(event, api) {
                        var rect = cy.container().getBoundingClientRect();
                        var pos = addedEntities.renderedBoundingBox();
                        api.set('position.adjust.x', rect.left + ((pos.x1 + pos.x2) / 2) + window.pageXOffset);
                        api.set('position.adjust.y', rect.top + ((pos.y2 + pos.y1) / 2) + window.pageYOffset);
                    },
                    visible: function(event, api) {
                        setTimeout(function() {
                        $('.transition').focus();
                        $('.transition').on('keypress', function(e) {
                            if(e.which === 13) {
                                api.hide();
                            }
                        });
                        }, 1);
                    }
                }
            });
            addedEntities.qtip('api').show();
        },
        stop: function( sourceNode ) {
            // fired when edgehandles interaction is stopped (either complete with added edges or incomplete)
        }
        };

        cy.edgehandles(edgehandlesDefaults);
// cy.edgehandles('drawon');
        return cy;
    }

    this.getCytoscapeInstanceForService = function() {
        return cy;
    }
        }
})();