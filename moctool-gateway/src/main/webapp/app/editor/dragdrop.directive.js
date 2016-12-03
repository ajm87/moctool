var app = angular.module('moctoolApp').directive('draggable', Draggable);

    Draggable.$inject = ['$compile'];
function Draggable($compile){
  function makeDraggable(scope, element, attrs, $compile) {

    // var deletedImages = [];

    // jsPlumb.setContainer($('#zoomcontainer'));
    //   $(element).draggable({
    //     helper: "clone",
    //     revert: "invalid"
    //   });

    //   $('#delete').droppable({
    //     over: function(event, ui) {
    //         ui.draggable.remove();
    //     }
    //   });

    //   $( "#zoomcontainer" ).droppable({
    //       drop: function( event, ui ) {
    //               var newDiv = $(ui.helper).clone();
    //               var src = $(newDiv).attr('src').replace("Toolbox", scope.stateNum);
    //               newDiv.attr('src', src);
    //               newDiv.addClass('state');
    //               $(this).append(newDiv);
    //               jsPlumb.draggable($(newDiv));
    //               var exampleGreyEndpointOptions = {
    //                 isSource:true,
    //                 isTarget:true,
    //                 connectorOverlays: [ [ "PlainArrow", { location:0.98, paintStyle: {fill: '#000000'}, width: 10, length: 10, id: 'arrow' } ],
    //                                      [ "Label", {location: 0.5, id:"label", cssClass: 'connector-label'}] ],
    //                 connector: ['StateMachine', {curviness: -1, loopbackRadius: 20}],
    //                 endpoint: ['Dot', {radius: 5}],
    //                 paintStyle: {fill: '#000000'},
    //                 maxConnections: -1,
    //                 allowLoopback: true
    //               };
    //               jsPlumb.addEndpoint($(newDiv), exampleGreyEndpointOptions);
    //               $(newDiv).dblclick(function() {
    //                 jsPlumb.detachAllConnections(newDiv);
    //                 jsPlumb.removeAllEndpoints(newDiv);
    //                 jsPlumb.detach(newDiv);
    //                 newDiv.remove();
    //                 deletedImages.push($(newDiv).attr('src'));
    //                 console.log("Deleted images is now", deletedImages);
    //               });
    //               scope.stateNum++;
    //         }
    //   });
  } return {
    scope: {
      stateNum: '=draggable',
      contextMenuCallback: '&contextmenucallback'
    },
    link: makeDraggable
  };
}