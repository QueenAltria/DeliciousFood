var select = function(s) {
    return document.querySelector(s);
  },
  selectAll = function(s) {
    return document.querySelectorAll(s);
  },
  bubbleGroup = select('.bubbleGroup'),
  allBubbles = selectAll('.bubbleGroup use');

var bubbleTl = new TimelineMax();

for (var i = 0; i < allBubbles.length; i++) {

  var b = TweenMax.fromTo(allBubbles[i], randomBetween(20, 40), {
    x: randomBetweenTwo(110, 235),
    y: 200,
    rotation: 720
  }, {
    x: randomBetween(0, 400),
    y: -200,
    rotation: 0,
    transformOrigin: randomBetween(-1900, 1900) + '% 50%',
    repeat: -1,
    ease: Linear.easeNone
  })

  bubbleTl.add(b, (i + 1) / 10)
}
bubbleTl.time(20)

function randomBetween(min, max) {
  return Math.floor(Math.random() * (max - min + 1) + min);
}

function randomBetweenTwo(low, high) {
  if (Math.floor(Math.random() * 2) == 0) {
    return low;
  } else {
    return high
  }
}

//bubble inspired by
//https://codepen.io/chrisgannon/pen/BNGvGE
//From Chris Gannon

var path = document.querySelector('.green');
var length = path.getTotalLength();
console.log(length);