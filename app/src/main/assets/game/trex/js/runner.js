!function(){"use strict";function a(b,c){return a.instance_?a.instance_:(a.instance_=this,this.outerContainerEl=document.querySelector(b),this.containerEl=null,this.config=c||a.config,this.dimensions=a.defaultDimensions,this.canvas=null,this.canvasCtx=null,this.tRex=null,this.distanceMeter=null,this.distanceRan=0,this.highestScore=0,this.time=0,this.runningTime=0,this.msPerFrame=1e3/r,this.currentSpeed=this.config.SPEED,this.obstacles=[],this.started=!1,this.activated=!1,this.crashed=!1,this.paused=!1,this.resizeTimerId_=null,this.playCount=0,this.audioBuffer=null,this.soundFx={},this.audioContext=null,this.images={},this.imagesLoaded=0,void this.loadImages())}function b(a,b){return Math.floor(Math.random()*(b-a+1))+a}function c(a){}function d(b,c,d,e){var f=document.createElement("canvas");return f.className=e?a.classes.CANVAS+" "+e:a.classes.CANVAS,f.width=c,f.height=d,b.appendChild(f),f}function e(a,b,c,d){this.canvas=a,this.canvasCtx=a.getContext("2d"),this.canvasDimensions=d,this.textSprite=b,this.restartImg=c,this.draw()}function f(b,c,d){var e=(a.defaultDimensions.WIDTH+b.xPos,new j(c.xPos+1,c.yPos+1,c.config.WIDTH-2,c.config.HEIGHT-2)),f=new j(b.xPos+1,b.yPos+1,b.typeConfig.width*b.size-2,b.typeConfig.height-2);if(d&&h(d,e,f),i(e,f))for(var k=b.collisionBoxes,m=l.collisionBoxes,n=0;n<m.length;n++)for(var o=0;o<k.length;o++){var p=g(m[n],e),q=g(k[o],f),r=i(p,q);if(d&&h(d,p,q),r)return[p,q]}return!1}function g(a,b){return new j(a.x+b.x,a.y+b.y,a.width,a.height)}function h(a,b,c){a.save(),a.strokeStyle="#f00",a.strokeRect(b.x,b.y,b.width,b.height),a.strokeStyle="#0f0",a.strokeRect(c.x,c.y,c.width,c.height),a.restore()}function i(a,b){{var c=!1,d=(a.x,a.y,b.x);b.y}return a.x<d+b.width&&a.x+a.width>d&&a.y<b.y+b.height&&a.height+a.y>b.y&&(c=!0),c}function j(a,b,c,d){this.x=a,this.y=b,this.width=c,this.height=d}function k(a,c,d,e,f,g){this.canvasCtx=a,this.image=d,this.typeConfig=c,this.gapCoefficient=f,this.size=b(1,k.MAX_OBSTACLE_LENGTH),this.dimensions=e,this.remove=!1,this.xPos=0,this.yPos=this.typeConfig.yPos,this.width=0,this.collisionBoxes=[],this.gap=0,this.init(g)}function l(a,b){this.canvas=a,this.canvasCtx=a.getContext("2d"),this.image=b,this.xPos=0,this.yPos=0,this.groundYPos=0,this.currentFrame=0,this.currentAnimFrames=[],this.blinkDelay=0,this.animStartTime=0,this.timer=0,this.msPerFrame=1e3/r,this.config=l.config,this.status=l.status.WAITING,this.jumping=!1,this.jumpVelocity=0,this.reachedMinHeight=!1,this.speedDrop=!1,this.jumpCount=0,this.jumpspotX=0,this.init()}function m(a,b,c){this.canvas=a,this.canvasCtx=a.getContext("2d"),this.image=b,this.x=0,this.y=5,this.currentDistance=0,this.maxScore=0,this.highScore=0,this.container=null,this.digits=[],this.acheivement=!1,this.defaultString="",this.flashTimer=0,this.flashIterations=0,this.config=m.config,this.init(c)}function n(a,c,d){this.canvas=a,this.canvasCtx=this.canvas.getContext("2d"),this.image=c,this.containerWidth=d,this.xPos=d,this.yPos=0,this.remove=!1,this.cloudGap=b(n.config.MIN_CLOUD_GAP,n.config.MAX_CLOUD_GAP),this.init()}function o(a,b){this.image=b,this.canvas=a,this.canvasCtx=a.getContext("2d"),this.sourceDimensions={},this.dimensions=o.dimensions,this.sourceXPos=[0,this.dimensions.WIDTH],this.xPos=[],this.yPos=0,this.bumpThreshold=.5,this.setSourceDimensions(),this.draw()}function p(a,b,c,d){this.canvas=a,this.canvasCtx=this.canvas.getContext("2d"),this.config=p.config,this.dimensions=c,this.gapCoefficient=d,this.obstacles=[],this.horizonOffsets=[0,0],this.cloudFrequency=this.config.CLOUD_FREQUENCY,this.clouds=[],this.cloudImg=b.CLOUD,this.cloudSpeed=this.config.BG_CLOUD_SPEED,this.horizonImg=b.HORIZON,this.horizonLine=null,this.obstacleImgs={CACTUS_SMALL:b.CACTUS_SMALL,CACTUS_LARGE:b.CACTUS_LARGE},this.init()}window.Runner=a;var q=600,r=60,s=window.devicePixelRatio>1,t="ontouchstart"in window;a.config={ACCELERATION:.001,BG_CLOUD_SPEED:.2,BOTTOM_PAD:10,CLEAR_TIME:3e3,CLOUD_FREQUENCY:.5,GAMEOVER_CLEAR_TIME:750,GAP_COEFFICIENT:.6,GRAVITY:.6,INITIAL_JUMP_VELOCITY:12,MAX_CLOUDS:6,MAX_OBSTACLE_LENGTH:3,MAX_SPEED:12,MIN_JUMP_HEIGHT:35,MOBILE_SPEED_COEFFICIENT:1.2,RESOURCE_TEMPLATE_ID:"audio-resources",SPEED:6,SPEED_DROP_COEFFICIENT:3},a.defaultDimensions={WIDTH:q,HEIGHT:150},a.classes={CANVAS:"runner-canvas",CONTAINER:"runner-container",CRASHED:"crashed",ICON:"icon-offline",TOUCH_CONTROLLER:"controller"},a.imageSources={LDPI:[{name:"CACTUS_LARGE",id:"1x-obstacle-large"},{name:"CACTUS_SMALL",id:"1x-obstacle-small"},{name:"CLOUD",id:"1x-cloud"},{name:"HORIZON",id:"1x-horizon"},{name:"RESTART",id:"1x-restart"},{name:"TEXT_SPRITE",id:"1x-text"},{name:"TREX",id:"1x-trex"}],HDPI:[{name:"CACTUS_LARGE",id:"2x-obstacle-large"},{name:"CACTUS_SMALL",id:"2x-obstacle-small"},{name:"CLOUD",id:"2x-cloud"},{name:"HORIZON",id:"2x-horizon"},{name:"RESTART",id:"2x-restart"},{name:"TEXT_SPRITE",id:"2x-text"},{name:"TREX",id:"2x-trex"}]},a.sounds={BUTTON_PRESS:"offline-sound-press",HIT:"offline-sound-hit",SCORE:"offline-sound-reached"},a.keycodes={JUMP:{38:1,32:1},DUCK:{40:1},RESTART:{13:1}},a.events={ANIM_END:"webkitAnimationEnd",CLICK:"click",KEYDOWN:"keydown",KEYUP:"keyup",MOUSEDOWN:"mousedown",MOUSEUP:"mouseup",RESIZE:"resize",TOUCHEND:"touchend",TOUCHSTART:"touchstart",VISIBILITY:"visibilitychange",BLUR:"blur",FOCUS:"focus",LOAD:"load"},a.prototype={updateConfigSetting:function(a,b){if(a in this.config&&void 0!=b)switch(this.config[a]=b,a){case"GRAVITY":case"MIN_JUMP_HEIGHT":case"SPEED_DROP_COEFFICIENT":this.tRex.config[a]=b;break;case"INITIAL_JUMP_VELOCITY":this.tRex.setJumpVelocity(b);break;case"SPEED":this.setSpeed(b)}},loadImages:function(){for(var b=s?a.imageSources.HDPI:a.imageSources.LDPI,c=b.length,d=c-1;d>=0;d--){var e=b[d];this.images[e.name]=document.getElementById(e.id)}this.init()},loadSounds:function(){},setSpeed:function(a){var b=a||this.currentSpeed;if(this.dimensions.WIDTH<q){var c=b*this.dimensions.WIDTH/q*this.config.MOBILE_SPEED_COEFFICIENT;this.currentSpeed=c>b?b:c}else a&&(this.currentSpeed=a)},init:function(){document.querySelector("."+a.classes.ICON).style.visibility="hidden",this.adjustDimensions(),this.setSpeed(),this.containerEl=document.createElement("div"),this.containerEl.className=a.classes.CONTAINER,this.canvas=d(this.containerEl,this.dimensions.WIDTH,this.dimensions.HEIGHT,a.classes.PLAYER),this.canvasCtx=this.canvas.getContext("2d"),this.canvasCtx.fillStyle="#f7f7f7",this.canvasCtx.fill(),a.updateCanvasScaling(this.canvas),this.horizon=new p(this.canvas,this.images,this.dimensions,this.config.GAP_COEFFICIENT),this.distanceMeter=new m(this.canvas,this.images.TEXT_SPRITE,this.dimensions.WIDTH),this.tRex=new l(this.canvas,this.images.TREX),this.outerContainerEl.appendChild(this.containerEl),t&&this.createTouchController(),this.startListening(),this.update(),window.addEventListener(a.events.RESIZE,this.debounceResize.bind(this))},createTouchController:function(){this.touchController=document.createElement("div"),this.touchController.className=a.classes.TOUCH_CONTROLLER},debounceResize:function(){this.resizeTimerId_||(this.resizeTimerId_=setInterval(this.adjustDimensions.bind(this),250))},adjustDimensions:function(){clearInterval(this.resizeTimerId_),this.resizeTimerId_=null;var b=window.getComputedStyle(this.outerContainerEl),c=Number(b.paddingLeft.substr(0,b.paddingLeft.length-2));this.dimensions.WIDTH=this.outerContainerEl.offsetWidth-2*c,this.canvas&&(this.canvas.width=this.dimensions.WIDTH,this.canvas.height=this.dimensions.HEIGHT,a.updateCanvasScaling(this.canvas),this.distanceMeter.calcXPos(this.dimensions.WIDTH),this.clearCanvas(),this.horizon.update(0,0,!0),this.tRex.update(0),this.activated||this.crashed?(this.containerEl.style.width=this.dimensions.WIDTH+"px",this.containerEl.style.height=this.dimensions.HEIGHT+"px",this.distanceMeter.update(0,Math.ceil(this.distanceRan)),this.stop()):this.tRex.draw(0,0),this.crashed&&this.gameOverPanel&&(this.gameOverPanel.updateDimensions(this.dimensions.WIDTH),this.gameOverPanel.draw()))},playIntro:function(){if(this.started||this.crashed)this.crashed&&this.restart();else{this.playingIntro=!0,this.tRex.playingIntro=!0;var a="intro { from { width:"+l.config.WIDTH+"px }to { width: "+this.dimensions.WIDTH+"px }}",b="@-webkit-keyframes "+a+" @keyframes "+a,c=document.createElement("style");c.textContent=b,document.body.appendChild(c);var d=document.createElement("fakeelement"),e={animation:"animationend",webkitAnimation:"webkitAnimationEnd",mozAnimation:"mozAnimationEnd",MSAnimation:"MSAnimationEnd"},f="animationEnd";for(var g in e)if(void 0!==d.style[g]){f=e[g];break}this.containerEl.addEventListener(f,this.startGame.bind(this)),this.containerEl.style.webkitAnimation="intro .4s ease-out 1 both",this.containerEl.style.animation="intro .4s ease-out 1 both",this.containerEl.style.width=this.dimensions.WIDTH+"px",this.touchController&&this.outerContainerEl.appendChild(this.touchController),this.activated=!0,this.started=!0}},startGame:function(){this.runningTime=0,this.playingIntro=!1,this.tRex.playingIntro=!1,this.containerEl.style.webkitAnimation="",this.containerEl.style.animation="",this.playCount++,window.addEventListener(a.events.VISIBILITY,this.onVisibilityChange.bind(this)),window.addEventListener(a.events.BLUR,this.onVisibilityChange.bind(this)),window.addEventListener(a.events.FOCUS,this.onVisibilityChange.bind(this))},clearCanvas:function(){this.canvasCtx.clearRect(0,0,this.dimensions.WIDTH,this.dimensions.HEIGHT)},update:function(){this.drawPending=!1;var a=new Date,b=a-(this.time||a);if(this.time=a,this.activated){this.clearCanvas(),this.tRex.jumping&&this.tRex.updateJump(b,this.config),this.runningTime+=b;var c=this.runningTime>this.config.CLEAR_TIME;1!=this.tRex.jumpCount||this.playingIntro||this.playIntro(),this.playingIntro?this.horizon.update(0,this.currentSpeed,c):(b=this.started?b:0,this.horizon.update(b,this.currentSpeed,c));var d=c&&f(this.horizon.obstacles[0],this.tRex);d?this.gameOver():(this.distanceRan+=this.currentSpeed*b/this.msPerFrame,this.currentSpeed<this.config.MAX_SPEED&&(this.currentSpeed+=this.config.ACCELERATION)),this.distanceMeter.getActualDistance(this.distanceRan)>this.distanceMeter.maxScore&&(this.distanceRan=0);var e=this.distanceMeter.update(b,Math.ceil(this.distanceRan));e&&this.playSound(this.soundFx.SCORE)}this.crashed||(this.tRex.update(b),this.raq())},handleEvent:function(b){return function(a,c){switch(a){case c.KEYDOWN:case c.TOUCHSTART:case c.MOUSEDOWN:this.onKeyDown(b);break;case c.KEYUP:case c.TOUCHEND:case c.MOUSEUP:this.onKeyUp(b)}}.bind(this)(b.type,a.events)},startListening:function(){document.addEventListener(a.events.KEYDOWN,this),document.addEventListener(a.events.KEYUP,this),t?(this.touchController.addEventListener(a.events.TOUCHSTART,this),this.touchController.addEventListener(a.events.TOUCHEND,this),this.containerEl.addEventListener(a.events.TOUCHSTART,this)):(document.addEventListener(a.events.MOUSEDOWN,this),document.addEventListener(a.events.MOUSEUP,this))},stopListening:function(){document.removeEventListener(a.events.KEYDOWN,this),document.removeEventListener(a.events.KEYUP,this),t?(this.touchController.removeEventListener(a.events.TOUCHSTART,this),this.touchController.removeEventListener(a.events.TOUCHEND,this),this.containerEl.removeEventListener(a.events.TOUCHSTART,this)):(document.removeEventListener(a.events.MOUSEDOWN,this),document.removeEventListener(a.events.MOUSEUP,this))},onKeyDown:function(b){this.crashed||!a.keycodes.JUMP[String(b.keyCode)]&&b.type!=a.events.TOUCHSTART||(this.activated||(this.loadSounds(),this.activated=!0),this.tRex.jumping||(this.playSound(this.soundFx.BUTTON_PRESS),this.tRex.startJump())),this.crashed&&b.type==a.events.TOUCHSTART&&b.currentTarget==this.containerEl&&this.restart(),a.keycodes.DUCK[b.keyCode]&&this.tRex.jumping&&(b.preventDefault(),this.tRex.setSpeedDrop())},onKeyUp:function(b){var c=String(b.keyCode),d=a.keycodes.JUMP[c]||b.type==a.events.TOUCHEND||b.type==a.events.MOUSEDOWN;if(this.isRunning()&&d)this.tRex.endJump();else if(a.keycodes.DUCK[c])this.tRex.speedDrop=!1;else if(this.crashed){var e=new Date-this.time;(a.keycodes.RESTART[c]||b.type==a.events.MOUSEUP&&b.target==this.canvas||e>=this.config.GAMEOVER_CLEAR_TIME&&a.keycodes.JUMP[c])&&this.restart()}else this.paused&&d&&this.play()},raq:function(){this.drawPending||(this.drawPending=!0,window.requestAnimationFrame?this.raqId=requestAnimationFrame(this.update.bind(this)):this.raqId=setTimeout(this.update.bind(this),0))},isRunning:function(){return!!this.raqId},gameOver:function(){this.playSound(this.soundFx.HIT),c(200),this.stop(),this.crashed=!0,this.distanceMeter.acheivement=!1,this.tRex.update(100,l.status.CRASHED),this.gameOverPanel?this.gameOverPanel.draw():this.gameOverPanel=new e(this.canvas,this.images.TEXT_SPRITE,this.images.RESTART,this.dimensions),this.distanceRan>this.highestScore&&(this.highestScore=Math.ceil(this.distanceRan),this.distanceMeter.setHighScore(this.highestScore)),this.time=new Date},stop:function(){this.activated=!1,this.paused=!0,window.requestAnimationFrame?cancelAnimationFrame(this.raqId):clearTimeout(this.raqId),this.raqId=0},play:function(){this.crashed||(this.activated=!0,this.paused=!1,this.tRex.update(0,l.status.RUNNING),this.time=new Date,this.update())},restart:function(){this.raqId||(this.playCount++,this.runningTime=0,this.activated=!0,this.crashed=!1,this.distanceRan=0,this.setSpeed(this.config.SPEED),this.time=new Date,this.containerEl.classList.remove(a.classes.CRASHED),this.clearCanvas(),this.distanceMeter.reset(this.highestScore),this.horizon.reset(),this.tRex.reset(),this.playSound(this.soundFx.BUTTON_PRESS),this.update())},onVisibilityChange:function(a){document.hidden||document.webkitHidden||"blur"==a.type?this.stop():this.play()},playSound:function(a){if(a){var b=this.audioContext.createBufferSource();b.buffer=a,b.connect(this.audioContext.destination),b.start(0)}}},a.updateCanvasScaling=function(a,b,c){var d=a.getContext("2d"),e=Math.floor(window.devicePixelRatio)||1,f=Math.floor(d.webkitBackingStorePixelRatio||d.backingStorePixelRatio)||1,g=e/f;if(e!==f){var h=b||a.width,i=c||a.height;return a.width=h*g,a.height=i*g,a.style.width=h+"px",a.style.height=i+"px",d.scale(g,g),!0}return!1},e.dimensions={TEXT_X:0,TEXT_Y:13,TEXT_WIDTH:191,TEXT_HEIGHT:11,RESTART_WIDTH:36,RESTART_HEIGHT:32},e.prototype={updateDimensions:function(a,b){this.canvasDimensions.WIDTH=a,b&&(this.canvasDimensions.HEIGHT=b)},draw:function(){var a=e.dimensions,b=this.canvasDimensions.WIDTH/2,c=a.TEXT_X,d=a.TEXT_Y,f=a.TEXT_WIDTH,g=a.TEXT_HEIGHT,h=Math.round(b-a.TEXT_WIDTH/2),i=Math.round((this.canvasDimensions.HEIGHT-25)/3),j=a.TEXT_WIDTH,k=a.TEXT_HEIGHT,l=a.RESTART_WIDTH,m=a.RESTART_HEIGHT,n=b-a.RESTART_WIDTH/2,o=this.canvasDimensions.HEIGHT/2;s&&(d*=2,c*=2,f*=2,g*=2,l*=2,m*=2),this.canvasCtx.drawImage(this.textSprite,c,d,f,g,h,i,j,k),this.canvasCtx.drawImage(this.restartImg,0,0,l,m,n,o,a.RESTART_WIDTH,a.RESTART_HEIGHT)}},k.MAX_GAP_COEFFICIENT=1.5,k.MAX_OBSTACLE_LENGTH=3,k.prototype={init:function(a){this.cloneCollisionBoxes(),this.size>1&&this.typeConfig.multipleSpeed>a&&(this.size=1),this.width=this.typeConfig.width*this.size,this.xPos=this.dimensions.WIDTH-this.width,this.draw(),this.size>1&&(this.collisionBoxes[1].width=this.width-this.collisionBoxes[0].width-this.collisionBoxes[2].width,this.collisionBoxes[2].x=this.width-this.collisionBoxes[2].width),this.gap=this.getGap(this.gapCoefficient,a)},draw:function(){var a=this.typeConfig.width,b=this.typeConfig.height;s&&(a=2*a,b=2*b);var c=a*this.size*.5*(this.size-1);this.canvasCtx.drawImage(this.image,c,0,a*this.size,b,this.xPos,this.yPos,this.typeConfig.width*this.size,this.typeConfig.height)},update:function(a,b){this.remove||(this.xPos-=Math.floor(b*r/1e3*a),this.draw(),this.isVisible()||(this.remove=!0))},getGap:function(a,c){var d=Math.round(this.width*c+this.typeConfig.minGap*a),e=Math.round(d*k.MAX_GAP_COEFFICIENT);return b(d,e)},isVisible:function(){return this.xPos+this.width>0},cloneCollisionBoxes:function(){for(var a=this.typeConfig.collisionBoxes,b=a.length-1;b>=0;b--)this.collisionBoxes[b]=new j(a[b].x,a[b].y,a[b].width,a[b].height)}},k.types=[{type:"CACTUS_SMALL",className:" cactus cactus-small ",width:17,height:35,yPos:105,multipleSpeed:3,minGap:120,collisionBoxes:[new j(0,7,5,27),new j(4,0,6,34),new j(10,4,7,14)]},{type:"CACTUS_LARGE",className:" cactus cactus-large ",width:25,height:50,yPos:90,multipleSpeed:6,minGap:120,collisionBoxes:[new j(0,12,7,38),new j(8,0,7,49),new j(13,10,10,38)]}],l.config={DROP_VELOCITY:-5,GRAVITY:.6,HEIGHT:47,INIITAL_JUMP_VELOCITY:-10,INTRO_DURATION:1500,MAX_JUMP_HEIGHT:30,MIN_JUMP_HEIGHT:30,SPEED_DROP_COEFFICIENT:3,SPRITE_WIDTH:262,START_X_POS:50,WIDTH:44},l.collisionBoxes=[new j(1,-1,30,26),new j(32,0,8,16),new j(10,35,14,8),new j(1,24,29,5),new j(5,30,21,4),new j(9,34,15,4)],l.status={CRASHED:"CRASHED",JUMPING:"JUMPING",RUNNING:"RUNNING",WAITING:"WAITING"},l.BLINK_TIMING=7e3,l.animFrames={WAITING:{frames:[44,0],msPerFrame:1e3/3},RUNNING:{frames:[88,132],msPerFrame:1e3/12},CRASHED:{frames:[220],msPerFrame:1e3/60},JUMPING:{frames:[0],msPerFrame:1e3/60}},l.prototype={init:function(){this.blinkDelay=this.setBlinkDelay(),this.groundYPos=a.defaultDimensions.HEIGHT-this.config.HEIGHT-a.config.BOTTOM_PAD,this.yPos=this.groundYPos,this.minJumpHeight=this.groundYPos-this.config.MIN_JUMP_HEIGHT,this.draw(0,0),this.update(0,l.status.WAITING)},setJumpVelocity:function(a){this.config.INIITAL_JUMP_VELOCITY=-a,this.config.DROP_VELOCITY=-a/2},update:function(a,b){this.timer+=a,b&&(this.status=b,this.currentFrame=0,this.msPerFrame=l.animFrames[b].msPerFrame,this.currentAnimFrames=l.animFrames[b].frames,b==l.status.WAITING&&(this.animStartTime=new Date,this.setBlinkDelay())),this.playingIntro&&this.xPos<this.config.START_X_POS&&(this.xPos+=Math.round(this.config.START_X_POS/this.config.INTRO_DURATION*a)),this.status==l.status.WAITING?this.blink(new Date):this.draw(this.currentAnimFrames[this.currentFrame],0),this.timer>=this.msPerFrame&&(this.currentFrame=this.currentFrame==this.currentAnimFrames.length-1?0:this.currentFrame+1,this.timer=0)},draw:function(a,b){var c=a,d=b,e=this.config.WIDTH,f=this.config.HEIGHT;s&&(c*=2,d*=2,e*=2,f*=2),this.canvasCtx.drawImage(this.image,c,d,e,f,this.xPos,this.yPos,this.config.WIDTH,this.config.HEIGHT)},setBlinkDelay:function(){this.blinkDelay=Math.ceil(Math.random()*l.BLINK_TIMING)},blink:function(a){var b=a-this.animStartTime;b>=this.blinkDelay&&(this.draw(this.currentAnimFrames[this.currentFrame],0),1==this.currentFrame&&(this.setBlinkDelay(),this.animStartTime=a))},startJump:function(){this.jumping||(this.update(0,l.status.JUMPING),this.jumpVelocity=this.config.INIITAL_JUMP_VELOCITY,this.jumping=!0,this.reachedMinHeight=!1,this.speedDrop=!1)},endJump:function(){this.reachedMinHeight&&this.jumpVelocity<this.config.DROP_VELOCITY&&(this.jumpVelocity=this.config.DROP_VELOCITY)},updateJump:function(a){var b=l.animFrames[this.status].msPerFrame,c=a/b;this.speedDrop?this.yPos+=Math.round(this.jumpVelocity*this.config.SPEED_DROP_COEFFICIENT*c):this.yPos+=Math.round(this.jumpVelocity*c),this.jumpVelocity+=this.config.GRAVITY*c,(this.yPos<this.minJumpHeight||this.speedDrop)&&(this.reachedMinHeight=!0),(this.yPos<this.config.MAX_JUMP_HEIGHT||this.speedDrop)&&this.endJump(),this.yPos>this.groundYPos&&(this.reset(),this.jumpCount++),this.update(a)},setSpeedDrop:function(){this.speedDrop=!0,this.jumpVelocity=1},reset:function(){this.yPos=this.groundYPos,this.jumpVelocity=0,this.jumping=!1,this.update(0,l.status.RUNNING),this.midair=!1,this.speedDrop=!1,this.jumpCount=0}},m.dimensions={WIDTH:10,HEIGHT:13,DEST_WIDTH:11},m.yPos=[0,13,27,40,53,67,80,93,107,120],m.config={MAX_DISTANCE_UNITS:5,ACHIEVEMENT_DISTANCE:100,COEFFICIENT:.025,FLASH_DURATION:250,FLASH_ITERATIONS:3},m.prototype={init:function(a){var b="";this.calcXPos(a),this.maxScore=this.config.MAX_DISTANCE_UNITS;for(var c=0;c<this.config.MAX_DISTANCE_UNITS;c++)this.draw(c,0),this.defaultString+="0",b+="9";this.maxScore=parseInt(b)},calcXPos:function(a){this.x=a-m.dimensions.DEST_WIDTH*(this.config.MAX_DISTANCE_UNITS+1)},draw:function(a,b,c){var d=m.dimensions.WIDTH,e=m.dimensions.HEIGHT,f=m.dimensions.WIDTH*b,g=a*m.dimensions.DEST_WIDTH,h=this.y,i=m.dimensions.WIDTH,j=m.dimensions.HEIGHT;if(s&&(d*=2,e*=2,f*=2),this.canvasCtx.save(),c){var k=this.x-2*this.config.MAX_DISTANCE_UNITS*m.dimensions.WIDTH;this.canvasCtx.translate(k,this.y)}else this.canvasCtx.translate(this.x,this.y);this.canvasCtx.drawImage(this.image,f,0,d,e,g,h,i,j),this.canvasCtx.restore()},getActualDistance:function(a){return a?Math.round(a*this.config.COEFFICIENT):0},update:function(a,b){var c=!0,d=!1;if(this.acheivement)this.flashIterations<=this.config.FLASH_ITERATIONS?(this.flashTimer+=a,this.flashTimer<this.config.FLASH_DURATION?c=!1:this.flashTimer>2*this.config.FLASH_DURATION&&(this.flashTimer=0,this.flashIterations++)):(this.acheivement=!1,this.flashIterations=0,this.flashTimer=0);else if(b=this.getActualDistance(b),b>0){b%this.config.ACHIEVEMENT_DISTANCE==0&&(this.acheivement=!0,this.flashTimer=0,d=!0);var e=(this.defaultString+b).substr(-this.config.MAX_DISTANCE_UNITS);this.digits=e.split("")}else this.digits=this.defaultString.split("");if(c)for(var f=this.digits.length-1;f>=0;f--)this.draw(f,parseInt(this.digits[f]));return this.drawHighScore(),d},drawHighScore:function(){this.canvasCtx.save(),this.canvasCtx.globalAlpha=.8;for(var a=this.highScore.length-1;a>=0;a--)this.draw(a,parseInt(this.highScore[a],10),!0);this.canvasCtx.restore()},setHighScore:function(a){a=this.getActualDistance(a);var b=(this.defaultString+a).substr(-this.config.MAX_DISTANCE_UNITS);this.highScore=["10","11",""].concat(b.split(""))},reset:function(){this.update(0),this.acheivement=!1}},n.config={HEIGHT:13,MAX_CLOUD_GAP:400,MAX_SKY_LEVEL:30,MIN_CLOUD_GAP:100,MIN_SKY_LEVEL:71,WIDTH:46},n.prototype={init:function(){this.yPos=b(n.config.MAX_SKY_LEVEL,n.config.MIN_SKY_LEVEL),this.draw()},draw:function(){this.canvasCtx.save();var a=n.config.WIDTH,b=n.config.HEIGHT;s&&(a=2*a,b=2*b),this.canvasCtx.drawImage(this.image,0,0,a,b,this.xPos,this.yPos,n.config.WIDTH,n.config.HEIGHT),this.canvasCtx.restore()},update:function(a){this.remove||(this.xPos-=Math.ceil(a),this.draw(),this.isVisible()||(this.remove=!0))},isVisible:function(){return this.xPos+n.config.WIDTH>0}},o.dimensions={WIDTH:600,HEIGHT:12,YPOS:127},o.prototype={setSourceDimensions:function(){for(var a in o.dimensions)s?"YPOS"!=a&&(this.sourceDimensions[a]=2*o.dimensions[a]):this.sourceDimensions[a]=o.dimensions[a],this.dimensions[a]=o.dimensions[a];this.xPos=[0,o.dimensions.WIDTH],this.yPos=o.dimensions.YPOS},getRandomType:function(){return Math.random()>this.bumpThreshold?this.dimensions.WIDTH:0},draw:function(){this.canvasCtx.drawImage(this.image,this.sourceXPos[0],0,this.sourceDimensions.WIDTH,this.sourceDimensions.HEIGHT,this.xPos[0],this.yPos,this.dimensions.WIDTH,this.dimensions.HEIGHT),this.canvasCtx.drawImage(this.image,this.sourceXPos[1],0,this.sourceDimensions.WIDTH,this.sourceDimensions.HEIGHT,this.xPos[1],this.yPos,this.dimensions.WIDTH,this.dimensions.HEIGHT)},updateXPos:function(a,b){var c=a,d=0==a?1:0;this.xPos[c]-=b,this.xPos[d]=this.xPos[c]+this.dimensions.WIDTH,this.xPos[c]<=-this.dimensions.WIDTH&&(this.xPos[c]+=2*this.dimensions.WIDTH,this.xPos[d]=this.xPos[c]-this.dimensions.WIDTH,this.sourceXPos[c]=this.getRandomType())},update:function(a,b){var c=Math.floor(b*(r/1e3)*a);this.xPos[0]<=0?this.updateXPos(0,c):this.updateXPos(1,c),this.draw()},reset:function(){this.xPos[0]=0,this.xPos[1]=o.dimensions.WIDTH}},p.config={BG_CLOUD_SPEED:.2,BUMPY_THRESHOLD:.3,CLOUD_FREQUENCY:.5,HORIZON_HEIGHT:16,MAX_CLOUDS:6},p.prototype={init:function(){this.addCloud(),this.horizonLine=new o(this.canvas,this.horizonImg)},update:function(a,b,c){this.runningTime+=a,this.horizonLine.update(a,b),this.updateClouds(a,b),c&&this.updateObstacles(a,b)},updateClouds:function(a,b){var c=this.cloudSpeed/1e3*a*b,d=this.clouds.length;if(d){for(var e=d-1;e>=0;e--)this.clouds[e].update(c);var f=this.clouds[d-1];d<this.config.MAX_CLOUDS&&this.dimensions.WIDTH-f.xPos>f.cloudGap&&this.cloudFrequency>Math.random()&&this.addCloud(),this.clouds=this.clouds.filter(function(a){return!a.remove})}},updateObstacles:function(a,b){for(var c=this.obstacles.slice(0),d=0;d<this.obstacles.length;d++){var e=this.obstacles[d];e.update(a,b),e.remove&&c.shift()}if(this.obstacles=c,this.obstacles.length>0){var f=this.obstacles[this.obstacles.length-1];f&&!f.followingObstacleCreated&&f.isVisible()&&f.xPos+f.width+f.gap<this.dimensions.WIDTH&&(this.addNewObstacle(b),f.followingObstacleCreated=!0)}else this.addNewObstacle(b)},addNewObstacle:function(a){var c=b(0,k.types.length-1),d=k.types[c],e=this.obstacleImgs[d.type];this.obstacles.push(new k(this.canvasCtx,d,e,this.dimensions,this.gapCoefficient,a))},reset:function(){this.obstacles=[],this.horizonLine.reset()},resize:function(a,b){this.canvas.width=a,this.canvas.height=b},addCloud:function(){this.clouds.push(new n(this.canvas,this.cloudImg,this.dimensions.WIDTH))}}}(),new Runner(".interstitial-wrapper");