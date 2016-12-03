(function() {
    'use strict';
    
    angular
        .module('moctoolApp')
        .service('CytoscapeService', CytoscapeService);

        CytoscapeService.$inject = ['$compile'];

        function CytoscapeService($compile) {
            var cy = cytoscape({
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
                        'label': 'data(label)'
                    }
                },
                {
                    selector: 'node',
                    style: {
                        'width': '50px',
                        'height': '50px',
                        'content': 'data(name)',
                        'text-valign': 'center',
                        'text-halign': 'center'
                    }
                }
                ]
        });

 var ctxOptions = {
    // List of initial menu items
    menuItems: [
      {
        id: 'initial', // ID of menu item
        title: 'Mark as initial', // Title of menu item
        // Filters the elements to have this menu item on cxttap
        // If the selector is not truthy no elements will have this menu item on cxttap
        selector: 'node', 
        onClickFunction: function (event) { // The function to be executed on click
            event.cyTarget.data('initial', 'true');
        },
        disabled: false, // Whether the item will be created as disabled
        coreAsWell: false // Whether core instance have this item on cxttap
      },
      {
        id: 'accept',
        title: 'Mark as accept state',
        selector: 'node',
        onClickFunction: function (event) {
            event.cyTarget.data('accept', 'true');
        },
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
      // add class names to this list
    ],
    // css classes that context menu will have
    contextMenuClasses: [
      // add class names to this list
    ]
};

cy.contextMenus( ctxOptions );

    this.getCytoscapeInstance = function($scope) {

        // the default values of each option are outlined below:
        var edgehandlesDefaults = {
        preview: true, // whether to show added edges preview before releasing selection
        stackOrder: 4, // Controls stack order of edgehandles canvas element by setting it's z-index
        handleSize: 10, // the size of the edge handle put on nodes
        handleColor: '#ff0000', // the colour of the handle and the line drawn from it
        handleLineType: 'ghost', // can be 'ghost' for real edge, 'straight' for a straight line, or 'draw' for a draw-as-you-go line
        handleLineWidth: 1, // width of handle line in pixels
        handleIcon: false, // Pass an Image-object to use as icon on handle. Icons are resized according to zoom and centered in handle.
        handleNodes: 'node', // selector/filter function for whether edges can be made from a given node
        hoverDelay: 150, // time spend over a target node before it is considered a target selection
        cxt: false, // whether cxt events trigger edgehandles (useful on touch)
        enabled: true, // whether to start the extension in the enabled state
        toggleOffOnLeave: false, // whether an edge is cancelled by leaving a node (true), or whether you need to go over again to cancel (false; allows multiple edges in one pass)
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
                        var ele = angular.element('<div class="form-group"><label>Input a symbol for this transition:<br><span style="font-size: 0.75em">(Leave blank for an &epsilon; transition)</span></label><div><input type="text" ng-model="symbol"><div><button type="button" class="btn btn-primary" ng-click="vm.setSymbol(connector, symbol)">OK</button><button type="button" class="btn btn-danger" ng-click="vm.cancelConnection(connector)">Cancel</button></div>');
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
                }
            });
        },
        stop: function( sourceNode ) {
            // fired when edgehandles interaction is stopped (either complete with added edges or incomplete)
        }
        };

        cy.edgehandles(edgehandlesDefaults);
// cy.edgehandles('drawon');
        return cy;
    }
        }
})();