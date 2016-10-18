var app = angular.module('moctoolApp').directive('draggable', Draggable);

Draggable.$inject = ['$document', '$window'];
function Draggable($document, $window){
  function makeDraggable(scope, element, attr) {
    var startX = 0;
    var startY = 0;

    // get element starting coords
    var x = element[0].getBoundingClientRect().left;
    var y = element[0].getBoundingClientRect().top;

    element.css({
      position: 'absolute',
      cursor: 'pointer'
    });

    element.on('mousedown', function(event) {
      event.preventDefault();
      
      startX = event.pageX - x;
      startY = event.pageY - y;

      $document.on('mousemove', mousemove);
      $document.on('mouseup', mouseup);
    });

    function mousemove(event) {
      y = event.pageY - startY;
      x = event.pageX - startX;

      element.css({
        top: y + 'px',
        left: x + 'px'
      });
    }

    function mouseup() {
      $document.unbind('mousemove', mousemove);
      $document.unbind('mouseup', mouseup);
    }
  }
  return {
    link: makeDraggable
  };
}