var app = angular.module('moctoolApp').directive('draggable', Draggable);

function Draggable(){
  function makeDraggable(scope, element, attrs) {
    //TODO: Rewrite

      $(element).mousedown(function() {
        $(element).addClass('original');
      });

      $(element).draggable({
        helper: "clone",
        revert: "invalid"
      });

      $( "#zoomcontainer" ).droppable({
          drop: function( event, ui ) {
              //if(ui.helper.hasClass('original')){
                  // ui.helper.removeClass('ui-draggable-dragging');
                  //var newDiv = $(ui.helper).clone().removeClass('ui-draggable-dragging').removeClass('original').addClass('drag');
                  var newDiv = $(ui.helper).clone();
                  var src = $(newDiv).attr('src').replace("Toolbox", scope.stateNum);
                  $(newDiv).attr('src', src);
                  $(this).append(newDiv);
                  jsPlumb.draggable($(newDiv));
                  var endpointOptions = {isSource: true, isTarget: true};
                  jsPlumb.addEndpoint($(newDiv), endpointOptions);
                  scope.stateNum++;
              //}
            }  
      });
  } return {
    scope: {
      stateNum: '=draggable'
    },
    link: makeDraggable
  };
}