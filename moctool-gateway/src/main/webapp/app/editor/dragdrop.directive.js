var app = angular.module('moctoolApp').directive('draggable', Draggable);

Draggable.$inject = ['$document', '$window'];
function Draggable($document, $window){
  function makeDraggable(scope, element, attrs) {
    var original = false;

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
    }
  // function makeDraggable(scope, element, attr) {
  //   var startX = 0;
  //   var startY = 0;

  //   // get element starting coords
  //   var x = element[0].getBoundingClientRect().left;
  //   var y = element[0].getBoundingClientRect().top;

  //   element.css({
  //     position: 'absolute',
  //     cursor: 'pointer'
  //   });

  //   element.on('mousedown', function(e) {
  //     e.preventDefault();
  //     startX = e.pageX - x;
  //     startY = e.pageY - y;

  //     $document.on('mousemove', mousemove);
  //     $document.on('mouseup', mouseup);
  //   });

  //   function mousemove(e) {
  //     y = e.pageY - startY;
  //     x = e.pageX - startX;

  //     element.css({
  //       top: y + 'px',
  //       left: x + 'px'
  //     });
  //   }

  //   function mouseup() {
  //     $document.unbind('mousemove', mousemove);
  //     $document.unbind('mouseup', mouseup);
  //   }
  // }
  return {
    link: makeDraggable
  };
}