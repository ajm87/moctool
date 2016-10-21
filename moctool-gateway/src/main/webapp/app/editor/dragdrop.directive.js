var app = angular.module('moctoolApp').directive('draggable', Draggable);

Draggable.$inject = ['$document', '$window'];
function Draggable($document, $window){
  function makeDraggable(scope, element, attrs) {
    //TODO: Rewrite
      $(element).mousedown(function() {
        console.log("original is now true");
        $(element).addClass('original');
      });

      $(element).draggable({
        helper: "clone",
        revert: "invalid"
      });

      $( "#panzoom" ).droppable({
          drop: function( event, ui ) {
              if(ui.helper.hasClass('original')){
                console.log("We original");
                  ui.helper.removeClass('ui-draggable-dragging');
                  console.log("original is now false");
                  var newDiv = $(ui.helper).clone().removeClass('ui-draggable-dragging').removeClass('original');
                  newDiv.draggable({
                    revert: "invalid"
                  });
                  $(this).children("div").children("div").append(newDiv);
              } else {
                console.log("We not original");
              }
            }  
      });
  } return {
    scope: {},
    link: makeDraggable
  };
}