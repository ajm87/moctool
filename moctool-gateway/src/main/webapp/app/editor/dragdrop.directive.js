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

      $( "#panzoom" ).droppable({
          drop: function( event, ui ) {
              if(ui.helper.hasClass('original')){
                  ui.helper.removeClass('ui-draggable-dragging');
                  var newDiv = $(ui.helper).clone().removeClass('ui-draggable-dragging').removeClass('original');
                  var src = $(newDiv).attr('src').replace("Toolbox", scope.stateNum);
                  $(newDiv).attr('src', src);
                  newDiv.draggable({
                    revert: "invalid"
                  });
                  $(this).children("div").children("div").append(newDiv);
                  scope.stateNum++;
              }
            }  
      });
  } return {
    scope: {
      stateNum: '=draggable'
    },
    link: makeDraggable
  };
}