var app = angular.module('moctoolApp').directive('draggable', Draggable);

Draggable.$inject = ['$document', '$window'];
function Draggable($document, $window){
  function makeDraggable(scope, element, attrs) {
    var original = false;
    //TODO: Rewrite
      $(element).mousedown(function() {
        original = true;
      });

      $(element).draggable({
        helper: "clone",
        revert: "invalid"
      });
      $( "#droponme" ).droppable({
          drop: function( event, ui ) {
              if(original){
                  ui.helper.removeClass('ui-draggable-dragging');
                  var newDiv = $(ui.helper).clone().removeClass('ui-draggable-dragging');
                  newDiv.draggable({
                    revert: "invalid"
                  });
                  $(this).append(newDiv);
                  original = false;
              }
            }  
      });
  } return {
    link: makeDraggable
  };
}