/*!
 * PhotoClip - 一款手势驱动的裁图插件
 * @version v3.4.8
 * @author baijunjie
 * @license MIT
 * 
 * git - https://github.com/baijunjie/PhotoClip.js.git
 */
(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory(require("hammerjs"), require("iscroll/build/iscroll-zoom"), require("lrz"));
	else if(typeof define === 'function' && define.amd)
		define(["hammerjs", "iscroll", "lrz"], factory);
	else if(typeof exports === 'object')
		exports["PhotoClip"] = factory(require("hammerjs"), require("iscroll/build/iscroll-zoom"), require("lrz"));
	else
		root["PhotoClip"] = factory(root["Hammer"], root["IScroll"], root["lrz"]);
})(this, function(__WEBPACK_EXTERNAL_MODULE__5__, __WEBPACK_EXTERNAL_MODULE__6__, __WEBPACK_EXTERNAL_MODULE__7__) {
return /******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 4);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  // 判断对象是否为数组
  module.exports = function (obj) {
    return Object.prototype.toString.call(obj) === '[object Array]';
  };
});

/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

  // 判断是否为一个对象
  module.exports = function (obj) {
    return _typeof(obj) === 'object';
  };
});

/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  // 判断是否为数字类型
  module.exports = function (num) {
    return typeof num === 'number';
  };
});

/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

  /**
   * 让隐藏元素正确执行程序（IE9及以上浏览器）
   * @param elems  {DOM|Array} DOM元素或者DOM元素组成的数组
   * @param func   {Function}  需要执行的程序函数
   * @param target {Object}    执行程序时函数中 this 的指向
   */
  var defaultDisplayMap = {};

  module.exports = function (elems, func, target) {
    if (_typeof(elems) !== 'object') {
      elems = [];
    }

    if (typeof elems.length === 'undefined') {
      elems = [elems];
    }

    var hideElems = [],
        hideElemsDisplay = [];

    for (var i = 0, elem; elem = elems[i++];) {
      while (elem instanceof HTMLElement) {
        var nodeName = elem.nodeName;

        if (!elem.getClientRects().length) {
          hideElems.push(elem);
          hideElemsDisplay.push(elem.style.display);
          var display = defaultDisplayMap[nodeName];

          if (!display) {
            var temp = document.createElement(nodeName);
            document.body.appendChild(temp);
            display = window.getComputedStyle(temp).display;
            temp.parentNode.removeChild(temp);
            if (display === 'none') display = 'block';
            defaultDisplayMap[nodeName] = display;
          }

          elem.style.display = display;
        }

        if (nodeName === 'BODY') break;
        elem = elem.parentNode;
      }
    }

    if (typeof func === 'function') func.call(target || this);
    var l = hideElems.length;

    while (l--) {
      hideElems.pop().style.display = hideElemsDisplay.pop();
    }
  };
});

/***/ }),
/* 4 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [exports, __webpack_require__(5), __webpack_require__(6), __webpack_require__(7), __webpack_require__(8), __webpack_require__(9), __webpack_require__(10), __webpack_require__(2), __webpack_require__(0), __webpack_require__(14), __webpack_require__(15), __webpack_require__(16), __webpack_require__(3), __webpack_require__(17), __webpack_require__(18), __webpack_require__(19), __webpack_require__(20), __webpack_require__(22)], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function (_exports, _hammerjs, _iscrollZoom, _lrz, _bind, _destroy2, _extend, _isNumber, _isArray, _isPercent, _createElement, _removeElement, _hideAction, _support, _css, _attr, _$, utils) {
  "use strict";

  Object.defineProperty(_exports, "__esModule", {
    value: true
  });
  _exports["default"] = void 0;
  _hammerjs = _interopRequireDefault(_hammerjs);
  _iscrollZoom = _interopRequireDefault(_iscrollZoom);
  _lrz = _interopRequireDefault(_lrz);
  _bind = _interopRequireDefault(_bind);
  _destroy2 = _interopRequireDefault(_destroy2);
  _extend = _interopRequireDefault(_extend);
  _isNumber = _interopRequireDefault(_isNumber);
  _isArray = _interopRequireDefault(_isArray);
  _isPercent = _interopRequireDefault(_isPercent);
  _createElement = _interopRequireDefault(_createElement);
  _removeElement = _interopRequireDefault(_removeElement);
  _hideAction = _interopRequireDefault(_hideAction);
  _support = _interopRequireDefault(_support);
  _css = _interopRequireDefault(_css);
  _attr = _interopRequireDefault(_attr);
  _$ = _interopRequireDefault(_$);
  utils = _interopRequireWildcard(utils);

  function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) { var desc = Object.defineProperty && Object.getOwnPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : {}; if (desc.get || desc.set) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } } newObj["default"] = obj; return newObj; } }

  function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

  function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

  function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

  function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

  function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

  var is_mobile = !!navigator.userAgent.match(/mobile/i),
      is_android = !!navigator.userAgent.match(/android/i),
      // 测试浏览器是否支持 Transition 动画，以及支持的前缀
  supportTransition = (0, _support["default"])('transition'),
      prefix = (0, _support["default"])('transform'),
      noop = function noop() {};

  var defaultOptions = {
    size: [100, 100],
    adaptive: '',
    outputSize: [0, 0],
    outputType: 'jpg',
    outputQuality: .8,
    maxZoom: 1,
    rotateFree: !is_android,
    view: '',
    file: '',
    ok: '',
    img: '',
    loadStart: noop,
    loadComplete: noop,
    loadError: noop,
    done: noop,
    fail: noop,
    lrzOption: {
      width: is_android ? 1000 : undefined,
      height: is_android ? 1000 : undefined,
      quality: .7
    },
    style: {
      maskColor: 'rgba(0,0,0,.5)',
      maskBorder: '2px dashed #ddd',
      jpgFillColor: '#fff'
    },
    errorMsg: {
      noSupport: '您的浏览器版本过于陈旧，无法支持裁图功能，请更换新的浏览器！',
      imgError: '不支持该图片格式，请选择常规格式的图片文件！',
      imgHandleError: '图片处理失败！请更换其它图片尝试。',
      imgLoadError: '图片读取失败！请更换其它图片尝试。',
      noImg: '没有可裁剪的图片！',
      clipError: '截图失败！当前图片源文件可能存在跨域问题，请确保图片与应用同源。如果您是在本地环境下执行本程序，请更换至服务器环境。'
    }
  };

  var PhotoClip =
  /*#__PURE__*/
  function () {
    function PhotoClip(container, options) {
      _classCallCheck(this, PhotoClip);

      container = (0, _$["default"])(container); // 获取容器

      if (container && container.length) {
        this._$container = container[0];
      } else {
        return;
      }

      this._options = (0, _extend["default"])(true, {}, defaultOptions, options);

      if (prefix === undefined) {
        this._options.errorMsg.noSupport && alert(this._options.errorMsg.noSupport);
      }

      this._init();
    }

    _createClass(PhotoClip, [{
      key: "_init",
      value: function _init() {
        var _this = this;

        var options = this._options; // options 预设

        if ((0, _isNumber["default"])(options.size)) {
          options.size = [options.size, options.size];
        } else if ((0, _isArray["default"])(options.size)) {
          if (!(0, _isNumber["default"])(options.size[0]) || options.size[0] <= 0) options.size[0] = defaultOptions.size[0];
          if (!(0, _isNumber["default"])(options.size[1]) || options.size[1] <= 0) options.size[1] = defaultOptions.size[1];
        } else {
          options.size = (0, _extend["default"])({}, defaultOptions.size);
        }

        if ((0, _isNumber["default"])(options.outputSize)) {
          options.outputSize = [options.outputSize, 0];
        } else if ((0, _isArray["default"])(options.outputSize)) {
          if (!(0, _isNumber["default"])(options.outputSize[0]) || options.outputSize[0] < 0) options.outputSize[0] = defaultOptions.outputSize[0];
          if (!(0, _isNumber["default"])(options.outputSize[1]) || options.outputSize[1] < 0) options.outputSize[1] = defaultOptions.outputSize[1];
        } else {
          options.outputSize = (0, _extend["default"])({}, defaultOptions.outputSize);
        }

        if (options.outputType === 'jpg') {
          options.outputType = 'image/jpeg';
        } else {
          // 如果不是 jpg，则全部按 png 来对待
          options.outputType = 'image/png';
        } // 变量初始化


        if ((0, _isArray["default"])(options.adaptive)) {
          this._widthIsPercent = options.adaptive[0] && (0, _isPercent["default"])(options.adaptive[0]) ? options.adaptive[0] : false;
          this._heightIsPercent = options.adaptive[1] && (0, _isPercent["default"])(options.adaptive[1]) ? options.adaptive[1] : false;
        }

        this._outputWidth = options.outputSize[0];
        this._outputHeight = options.outputSize[1];
        this._canvas = document.createElement('canvas'); // 图片裁剪用到的画布

        this._iScroll = null; // 图片的scroll对象，包含图片的位置与缩放信息

        this._hammerManager = null; // hammer 管理对象

        this._clipWidth = 0;
        this._clipHeight = 0;
        this._clipSizeRatio = 1; // 截取框宽高比

        this._$img = null; // 图片的DOM对象

        this._imgLoaded = false; // 图片是否已经加载完成

        this._containerWidth = 0;
        this._containerHeight = 0;
        this._viewList = null; // 最终截图后呈现的视图容器的DOM数组

        this._fileList = null; // file 控件的DOM数组

        this._okList = null; // 截图按钮的DOM数组

        this._$mask = null;
        this._$mask_left = null;
        this._$mask_right = null;
        this._$mask_right = null;
        this._$mask_bottom = null;
        this._$clip_frame = null;
        this._$clipLayer = null; // 裁剪层，包含移动层

        this._$moveLayer = null; // 移动层，包含旋转层

        this._$rotateLayer = null; // 旋转层

        this._moveLayerWidth = 0; // 移动层的宽度(不跟随scale发生变化)

        this._moveLayerHeight = 0; // 移动层的高度(不跟随scale发生变化)

        this._moveLayerPaddingLeft = 0; // 当图片宽度小于裁剪框宽度时，移动层的补偿左边距(不跟随scale发生变化)

        this._moveLayerPaddingTop = 0; // 当图片高度小于裁剪框高度时，移动层的补偿顶边距(不跟随scale发生变化)

        this._atRotation = false; // 旋转层是否正在旋转中

        this._rotateLayerWidth = 0; // 旋转层所呈现矩形的宽度(不跟随scale发生变化)

        this._rotateLayerHeight = 0; // 旋转层所呈现矩形的高度(不跟随scale发生变化)

        this._rotateLayerX = 0; // 旋转层的当前X坐标(不跟随scale发生变化)

        this._rotateLayerY = 0; // 旋转层的当前Y坐标(不跟随scale发生变化)

        this._rotateLayerOriginX = 0; // 旋转层的旋转参考点X(不跟随scale发生变化)

        this._rotateLayerOriginY = 0; // 旋转层的旋转参考点Y(不跟随scale发生变化)

        this._curAngle = 0; // 旋转层的当前角度

        (0, _bind["default"])(this, '_resetScroll', '_rotateCW90', '_fileOnChangeHandle', '_clipImg', '_resize', 'size', 'load', 'clear', 'rotate', 'scale', 'clip', 'destroy');

        this._initElements();

        this._initScroll();

        this._initRotationEvent();

        this._initFile();

        this._resize();

        window.addEventListener('resize', this._resize);

        if (this._okList = (0, _$["default"])(options.ok)) {
          this._okList.forEach(function ($ok) {
            $ok.addEventListener('click', _this._clipImg);
          });
        }

        if (this._options.img) {
          this._lrzHandle(this._options.img);
        }
      }
    }, {
      key: "_initElements",
      value: function _initElements() {
        // 初始化容器
        var $container = this._$container,
            style = $container.style,
            containerOriginStyle = {};
        containerOriginStyle['user-select'] = style['user-select'];
        containerOriginStyle['overflow'] = style['overflow'];
        containerOriginStyle['position'] = style['position'];
        this._containerOriginStyle = containerOriginStyle;
        (0, _css["default"])($container, {
          'user-select': 'none',
          'overflow': 'hidden'
        });

        if ((0, _css["default"])($container, 'position') === 'static') {
          (0, _css["default"])($container, 'position', 'relative');
        } // 创建裁剪层


        this._$clipLayer = (0, _createElement["default"])($container, 'photo-clip-layer', {
          'position': 'absolute',
          'left': '50%',
          'top': '50%'
        });
        this._$moveLayer = (0, _createElement["default"])(this._$clipLayer, 'photo-clip-move-layer');
        this._$rotateLayer = (0, _createElement["default"])(this._$moveLayer, 'photo-clip-rotate-layer'); // 创建遮罩

        var $mask = this._$mask = (0, _createElement["default"])($container, 'photo-clip-mask', {
          'position': 'absolute',
          'left': 0,
          'top': 0,
          'width': '100%',
          'height': '100%',
          'pointer-events': 'none'
        });
        var options = this._options,
            maskColor = options.style.maskColor,
            maskBorder = options.style.maskBorder;
        this._$mask_left = (0, _createElement["default"])($mask, 'photo-clip-mask-left', {
          'position': 'absolute',
          'left': 0,
          'right': '50%',
          'top': '50%',
          'bottom': '50%',
          'width': 'auto',
          'background-color': maskColor
        });
        this._$mask_right = (0, _createElement["default"])($mask, 'photo-clip-mask-right', {
          'position': 'absolute',
          'left': '50%',
          'right': 0,
          'top': '50%',
          'bottom': '50%',
          'background-color': maskColor
        });
        this._$mask_top = (0, _createElement["default"])($mask, 'photo-clip-mask-top', {
          'position': 'absolute',
          'left': 0,
          'right': 0,
          'top': 0,
          'bottom': '50%',
          'background-color': maskColor
        });
        this._$mask_bottom = (0, _createElement["default"])($mask, 'photo-clip-mask-bottom', {
          'position': 'absolute',
          'left': 0,
          'right': 0,
          'top': '50%',
          'bottom': 0,
          'background-color': maskColor
        }); // 创建截取框

        this._$clip_frame = (0, _createElement["default"])($mask, 'photo-clip-area', {
          'border': maskBorder,
          'position': 'absolute',
          'left': '50%',
          'top': '50%'
        }); // 初始化视图容器

        this._viewList = (0, _$["default"])(options.view);

        if (this._viewList) {
          var viewOriginStyleList = [];

          this._viewList.forEach(function ($view, i) {
            var style = $view.style,
                viewOriginStyle = {};
            viewOriginStyle['background-repeat'] = style['background-repeat'];
            viewOriginStyle['background-position'] = style['background-position'];
            viewOriginStyle['background-size'] = style['background-size'];
            viewOriginStyleList[i] = viewOriginStyle;
            (0, _css["default"])($view, {
              'background-repeat': 'no-repeat',
              'background-position': 'center',
              'background-size': 'contain'
            });
          });

          this._viewOriginStyleList = viewOriginStyleList;
        }
      }
    }, {
      key: "_initScroll",
      value: function _initScroll() {
        var _this2 = this;

        this._iScroll = new _iscrollZoom["default"](this._$clipLayer, {
          zoom: true,
          scrollX: true,
          scrollY: true,
          freeScroll: true,
          mouseWheel: true,
          disablePointer: true,
          // important to disable the pointer events that causes the issues
          disableTouch: false,
          // false if you want the slider to be usable with touch devices
          disableMouse: false,
          // false if you want the slider to be usable with a mouse (desktop)
          wheelAction: 'zoom',
          bounceTime: 300
        });

        this._iScroll.on('zoomEnd', function () {
          _this2._calcScale();

          _this2._resizeMoveLayer();

          _this2._refreshScroll();
        });
      } // 重置 iScroll

    }, {
      key: "_resetScroll",
      value: function _resetScroll() {
        var iScroll = this._iScroll;

        this._calcScale();

        var scale = iScroll.scale = iScroll.options.startZoom;

        this._resizeMoveLayer(); // 重置旋转层


        this._rotateLayerX = 0;
        this._rotateLayerY = 0;
        this._curAngle = 0;
        setTransform(this._$rotateLayer, this._rotateLayerX + this._moveLayerPaddingLeft, this._rotateLayerY + this._moveLayerPaddingTop, this._curAngle); // 初始化居中

        iScroll.scrollTo((this._clipWidth - this._moveLayerWidth * scale) * .5, (this._clipHeight - this._moveLayerHeight * scale) * .5);

        this._refreshScroll();
      } // 刷新 iScroll
      // duration 表示移动层超出容器时的复位动画持续时长

    }, {
      key: "_refreshScroll",
      value: function _refreshScroll(duration) {
        duration = duration || 0;
        var iScroll = this._iScroll,
            scale = iScroll.scale,
            iScrollOptions = iScroll.options;
        var lastScale = Math.max(iScrollOptions.zoomMin, Math.min(iScrollOptions.zoomMax, scale));

        if (lastScale !== scale) {
          iScroll.zoom(lastScale, undefined, undefined, duration);
        }

        iScroll.refresh(duration);
      } // 调整移动层

    }, {
      key: "_resizeMoveLayer",
      value: function _resizeMoveLayer() {
        var iScroll = this._iScroll,
            iScrollOptions = iScroll.options,
            scale = Math.max(iScrollOptions.zoomMin, Math.min(iScrollOptions.zoomMax, iScroll.scale));
        var width = this._rotateLayerWidth,
            height = this._rotateLayerHeight,
            clipWidth = this._clipWidth / scale,
            clipHeight = this._clipHeight / scale,
            ltClipArea = false;

        if (clipWidth > width) {
          ltClipArea = true;
          var offset = clipWidth - width;
          width += offset * 2;
          iScroll.x += (this._moveLayerPaddingLeft - offset) * scale;
          this._moveLayerPaddingLeft = offset;
        } else {
          this._moveLayerPaddingLeft = 0;
        }

        if (clipHeight > height) {
          ltClipArea = true;

          var _offset = clipHeight - height;

          height += _offset * 2;
          iScroll.y += (this._moveLayerPaddingTop - _offset) * scale;
          this._moveLayerPaddingTop = _offset;
        } else {
          this._moveLayerPaddingTop = 0;
        }

        if (ltClipArea) {
          setTransform(this._$rotateLayer, this._rotateLayerX + this._moveLayerPaddingLeft, this._rotateLayerY + this._moveLayerPaddingTop, this._curAngle);
          iScroll.scrollTo(iScroll.x, iScroll.y);
        }

        if (this._moveLayerWidth === width && this._moveLayerHeight === height) return;
        this._moveLayerWidth = width;
        this._moveLayerHeight = height;
        (0, _css["default"])(this._$moveLayer, {
          'width': width,
          'height': height
        }); // 在移动设备上，尤其是Android设备，当为一个元素重置了宽高时
        // 该元素的 offsetWidth/offsetHeight、clientWidth/clientHeight 等属性并不会立即更新，导致相关的js程序出现错误
        // iscroll 在刷新方法中正是使用了 offsetWidth/offsetHeight 来获取scroller元素($moveLayer)的宽高
        // 因此需要手动将元素重新添加进文档，迫使浏览器强制更新元素的宽高

        this._$clipLayer.appendChild(this._$moveLayer);
      }
    }, {
      key: "_calcScale",
      value: function _calcScale() {
        var iScroll = this._iScroll,
            iScrollOptions = iScroll.options,
            width = this._rotateLayerWidth,
            height = this._rotateLayerHeight,
            maxZoom = this._options.maxZoom;

        if (width && height) {
          iScrollOptions.zoomMin = Math.min(1, utils.getScale(this._clipWidth, this._clipHeight, width, height));
          iScrollOptions.zoomMax = maxZoom;
          iScrollOptions.startZoom = Math.min(maxZoom, utils.getScale(this._containerWidth, this._containerHeight, width, height));
        } else {
          iScrollOptions.zoomMin = 1;
          iScrollOptions.zoomMax = 1;
          iScrollOptions.startZoom = 1;
        } // console.log('zoomMin', iScrollOptions.zoomMin);
        // console.log('zoomMax', iScrollOptions.zoomMax);
        // console.log('startZoom', iScrollOptions.startZoom);

      }
    }, {
      key: "_initRotationEvent",
      value: function _initRotationEvent() {
        var _this3 = this;

        if (is_mobile) {
          this._hammerManager = new _hammerjs["default"].Manager(this._$moveLayer);

          this._hammerManager.add(new _hammerjs["default"].Rotate());

          var rotateFree = this._options.rotateFree,
              bounceTime = this._iScroll.options.bounceTime;
          var startTouch, startAngle, curAngle;

          this._hammerManager.on('rotatestart', function (e) {
            if (_this3._atRotation) return;
            startTouch = true;

            if (rotateFree) {
              startAngle = (e.rotation - _this3._curAngle) % 360;

              _this3._rotateLayerRotateReady(e.center);
            } else {
              startAngle = e.rotation;
            }
          });

          this._hammerManager.on('rotatemove', function (e) {
            if (!startTouch) return;
            curAngle = e.rotation - startAngle;
            rotateFree && _this3._rotateLayerRotate(curAngle);
          });

          this._hammerManager.on('rotateend rotatecancel', function (e) {
            if (!startTouch) return;
            startTouch = false;

            if (!rotateFree) {
              curAngle %= 360;
              if (curAngle > 180) curAngle -= 360;else if (curAngle < -180) curAngle += 360;

              if (curAngle > 30) {
                _this3._rotateBy(90, bounceTime, e.center);
              } else if (curAngle < -30) {
                _this3._rotateBy(-90, bounceTime, e.center);
              }

              return;
            } // 接近整90度方向时，进行校正


            var angle = curAngle % 360;
            if (angle < 0) angle += 360;

            if (angle < 10) {
              curAngle += -angle;
            } else if (angle > 80 && angle < 100) {
              curAngle += 90 - angle;
            } else if (angle > 170 && angle < 190) {
              curAngle += 180 - angle;
            } else if (angle > 260 && angle < 280) {
              curAngle += 270 - angle;
            } else if (angle > 350) {
              curAngle += 360 - angle;
            }

            _this3._rotateLayerRotateFinish(curAngle, bounceTime);
          });
        } else {
          this._$moveLayer.addEventListener('dblclick', this._rotateCW90);
        }
      }
    }, {
      key: "_rotateCW90",
      value: function _rotateCW90(e) {
        this._rotateBy(90, this._iScroll.options.bounceTime, {
          x: e.clientX,
          y: e.clientY
        });
      }
    }, {
      key: "_rotateBy",
      value: function _rotateBy(angle, duration, center) {
        this._rotateTo(this._curAngle + angle, duration, center);
      }
    }, {
      key: "_rotateTo",
      value: function _rotateTo(angle, duration, center) {
        if (this._atRotation) return;

        this._rotateLayerRotateReady(center); // 旋转层旋转结束


        this._rotateLayerRotateFinish(angle, duration);
      } // 旋转层旋转准备

    }, {
      key: "_rotateLayerRotateReady",
      value: function _rotateLayerRotateReady(center) {
        var scale = this._iScroll.scale;
        var coord; // 旋转参考点在移动层中的坐标

        if (!center) {
          coord = utils.loaclToLoacl(this._$moveLayer, this._$clipLayer, this._clipWidth * .5, this._clipHeight * .5);
        } else {
          coord = utils.globalToLoacl(this._$moveLayer, center.x, center.y);
        } // 由于得到的坐标是在缩放后坐标系上的坐标，因此需要除以缩放比例


        coord.x /= scale;
        coord.y /= scale; // 旋转参考点相对于旋转层零位（旋转层旋转前左上角）的坐标

        var coordBy0 = {
          x: coord.x - (this._rotateLayerX + this._moveLayerPaddingLeft),
          y: coord.y - (this._rotateLayerY + this._moveLayerPaddingTop)
        }; // 求出旋转层旋转前的旋转参考点
        // 这个参考点就是旋转中心点映射在旋转层图片上的坐标
        // 这个位置表示旋转层旋转前，该点所对应的坐标

        var origin = utils.pointRotate(coordBy0, -this._curAngle);
        this._rotateLayerOriginX = origin.x;
        this._rotateLayerOriginY = origin.y; // 设置参考点，算出新参考点作用下的旋转层位移，然后进行补差

        var rect = this._$rotateLayer.getBoundingClientRect();

        setOrigin(this._$rotateLayer, this._rotateLayerOriginX, this._rotateLayerOriginY);

        var newRect = this._$rotateLayer.getBoundingClientRect();

        this._rotateLayerX += (rect.left - newRect.left) / scale;
        this._rotateLayerY += (rect.top - newRect.top) / scale;
        setTransform(this._$rotateLayer, this._rotateLayerX + this._moveLayerPaddingLeft, this._rotateLayerY + this._moveLayerPaddingTop, this._curAngle);
      } // 旋转层旋转

    }, {
      key: "_rotateLayerRotate",
      value: function _rotateLayerRotate(angle) {
        setTransform(this._$rotateLayer, this._rotateLayerX + this._moveLayerPaddingLeft, this._rotateLayerY + this._moveLayerPaddingTop, angle);
        this._curAngle = angle;
      } // 旋转层旋转结束

    }, {
      key: "_rotateLayerRotateFinish",
      value: function _rotateLayerRotateFinish(angle, duration) {
        var _this4 = this;

        setTransform(this._$rotateLayer, this._rotateLayerX + this._moveLayerPaddingLeft, this._rotateLayerY + this._moveLayerPaddingTop, angle);
        var iScroll = this._iScroll,
            scale = iScroll.scale,
            iScrollOptions = iScroll.options; // 获取旋转后的矩形

        var rect = this._$rotateLayer.getBoundingClientRect(); // 更新旋转层当前所呈现矩形的宽高


        this._rotateLayerWidth = rect.width / scale;
        this._rotateLayerHeight = rect.height / scale; // 当参考点为零时，获取位移后的矩形

        setOrigin(this._$rotateLayer, 0, 0);

        var rectByOrigin0 = this._$rotateLayer.getBoundingClientRect(); // 获取旋转前（零度）的矩形


        setTransform(this._$rotateLayer, this._rotateLayerX + this._moveLayerPaddingLeft, this._rotateLayerY + this._moveLayerPaddingTop, 0);

        var rectByAngle0 = this._$rotateLayer.getBoundingClientRect(); // 当参考点为零时，旋转层旋转后，在形成的新矩形中，旋转层零位（旋转层旋转前左上角）的新坐标


        this._rotateLayerX = (rectByAngle0.left - rectByOrigin0.left) / scale;
        this._rotateLayerY = (rectByAngle0.top - rectByOrigin0.top) / scale;

        this._calcScale();

        this._resizeMoveLayer(); // 获取移动层的矩形


        var moveLayerRect = this._$moveLayer.getBoundingClientRect(); // 求出移动层与旋转层之间的位置偏移
        // 由于直接应用在移动层，因此不需要根据缩放换算
        // 注意，这里的偏移有可能还包含缩放过量时多出来的偏移


        var offset = {
          x: rect.left - this._moveLayerPaddingLeft * scale - moveLayerRect.left,
          y: rect.top - this._moveLayerPaddingTop * scale - moveLayerRect.top
        };
        iScroll.scrollTo(iScroll.x + offset.x, iScroll.y + offset.y);

        this._refreshScroll(iScroll.options.bounceTime); // 由于 offset 可能还包含缩放过量时多出来的偏移
        // 因此，这里判断是否缩放过量


        var lastScale = Math.max(iScrollOptions.zoomMin, Math.min(iScrollOptions.zoomMax, scale));

        if (lastScale !== scale) {
          // 当缩放过量时，将 offset 换算为最终的正常比例对应的值
          offset.x = offset.x / scale * lastScale;
          offset.y = offset.y / scale * lastScale;
        } // 由于双指旋转时也伴随着缩放，因此这里代码执行完后，将会执行 iscroll 的 _zoomEnd
        // 而该方法会基于 touchstart 时记录的位置重新计算 x、y，这将导致手指离开屏幕后，移动层又会向回移动一段距离
        // 所以这里也要将 startX、startY 这两个值进行补差，而这个差值必须是最终的正常比例对应的值


        iScroll.startX += offset.x;
        iScroll.startY += offset.y;

        if (angle !== this._curAngle && duration && (0, _isNumber["default"])(duration) && supportTransition !== undefined) {
          // 计算旋转层参考点，设为零位前后的偏移量
          offset = {
            x: (rectByOrigin0.left - rect.left) / scale,
            y: (rectByOrigin0.top - rect.top) / scale
          }; // 将旋转参考点设回前值，同时调整偏移量，保证视图位置不变，准备开始动画

          setOrigin(this._$rotateLayer, this._rotateLayerOriginX, this._rotateLayerOriginY);
          var targetX = this._rotateLayerX + this._moveLayerPaddingLeft + offset.x,
              targetY = this._rotateLayerY + this._moveLayerPaddingTop + offset.y;
          setTransform(this._$rotateLayer, targetX, targetY, this._curAngle); // 开始旋转

          this._atRotation = true;
          setTransition(this._$rotateLayer, targetX, targetY, angle, duration, function () {
            _this4._atRotation = false;

            _this4._rotateFinishUpdataElem(angle);
          });
        } else {
          this._rotateFinishUpdataElem(angle);
        }
      } // 旋转结束更新相关元素

    }, {
      key: "_rotateFinishUpdataElem",
      value: function _rotateFinishUpdataElem(angle) {
        setOrigin(this._$rotateLayer, this._rotateLayerOriginX = 0, this._rotateLayerOriginY = 0);
        setTransform(this._$rotateLayer, this._rotateLayerX + this._moveLayerPaddingLeft, this._rotateLayerY + this._moveLayerPaddingTop, this._curAngle = angle % 360);
      }
    }, {
      key: "_initFile",
      value: function _initFile() {
        var _this5 = this;

        var options = this._options;

        if (this._fileList = (0, _$["default"])(options.file)) {
          this._fileList.forEach(function ($file) {
            // 移动端如果设置 'accept'，会使相册打开缓慢，因此这里只为非移动端设置
            if (!is_mobile) {
              (0, _attr["default"])($file, 'accept', 'image/jpeg, image/x-png, image/png, image/gif');
            }

            $file.addEventListener('change', _this5._fileOnChangeHandle);
          });
        }
      }
    }, {
      key: "_fileOnChangeHandle",
      value: function _fileOnChangeHandle(e) {
        var files = e.target.files;

        if (files.length) {
          this._lrzHandle(files[0]);
        }
      }
    }, {
      key: "_lrzHandle",
      value: function _lrzHandle(src) {
        var _this6 = this;

        var options = this._options,
            errorMsg = options.errorMsg;

        if (_typeof(src) === 'object' && src.type && !/image\/\w+/.test(src.type)) {
          options.loadError.call(this, errorMsg.imgError);
          return false;
        }

        this._imgLoaded = false;
        options.loadStart.call(this, src);

        try {
          (0, _lrz["default"])(src, options.lrzOption).then(function (rst) {
            // 处理成功会执行
            _this6._clearImg();

            _this6._createImg(rst.base64);
          })["catch"](function (err) {
            // 处理失败会执行
            options.loadError.call(_this6, errorMsg.imgHandleError, err);
          });
        } catch (err) {
          options.loadError.call(this, errorMsg.imgHandleError, err);
          throw err;
        }
      }
    }, {
      key: "_clearImg",
      value: function _clearImg() {
        if (!this._$img) return; // 删除旧的图片以释放内存，防止IOS设备的 webview 崩溃

        this._$img.onload = null;
        this._$img.onerror = null;
        (0, _removeElement["default"])(this._$img);
        this._$img = null;
        this._imgLoaded = false;
      }
    }, {
      key: "_createImg",
      value: function _createImg(src) {
        var _this7 = this;

        var options = this._options,
            errorMsg = options.errorMsg;
        this._$img = new Image();
        (0, _css["default"])(this._$img, {
          'display': 'block',
          'user-select': 'none',
          'pointer-events': 'none'
        });

        this._$img.onload = function (e) {
          var img = e.target;
          _this7._imgLoaded = true;
          options.loadComplete.call(_this7, img);

          _this7._$rotateLayer.appendChild(img);

          _this7._rotateLayerWidth = img.naturalWidth;
          _this7._rotateLayerHeight = img.naturalHeight;
          (0, _css["default"])(_this7._$rotateLayer, {
            'width': _this7._rotateLayerWidth,
            'height': _this7._rotateLayerHeight
          });
          (0, _hideAction["default"])([img, _this7._$moveLayer], _this7._resetScroll);
        };

        this._$img.onerror = function (e) {
          options.loadError.call(_this7, errorMsg.imgLoadError, e);
        };

        (0, _attr["default"])(this._$img, 'src', src);
      }
    }, {
      key: "_clipImg",
      value: function _clipImg() {
        var options = this._options,
            errorMsg = options.errorMsg;

        if (!this._imgLoaded) {
          options.fail.call(this, errorMsg.noImg);
          return;
        }

        var local = utils.loaclToLoacl(this._$moveLayer, this._$clipLayer),
            scale = this._iScroll.scale,
            ctx = this._canvas.getContext('2d');

        var scaleX = 1,
            scaleY = 1;

        if (this._outputWidth || this._outputHeight) {
          this._canvas.width = this._outputWidth;
          this._canvas.height = this._outputHeight;
          scaleX = this._outputWidth / this._clipWidth * scale;
          scaleY = this._outputHeight / this._clipHeight * scale;
        } else {
          this._canvas.width = this._clipWidth / scale;
          this._canvas.height = this._clipHeight / scale;
        }

        ctx.clearRect(0, 0, this._canvas.width, this._canvas.height);
        ctx.fillStyle = options.outputType === 'image/png' ? 'transparent' : options.style.jpgFillColor;
        ctx.fillRect(0, 0, this._canvas.width, this._canvas.height);
        ctx.save();
        ctx.scale(scaleX, scaleY);
        ctx.translate(this._rotateLayerX + this._moveLayerPaddingLeft - local.x / scale, this._rotateLayerY + this._moveLayerPaddingTop - local.y / scale);
        ctx.rotate(this._curAngle * Math.PI / 180);
        ctx.drawImage(this._$img, 0, 0);
        ctx.restore();

        try {
          var dataURL = this._canvas.toDataURL(options.outputType, options.outputQuality);

          if (this._viewList) {
            this._viewList.forEach(function ($view) {
              (0, _css["default"])($view, 'background-image', "url(".concat(dataURL, ")"));
            });
          }

          options.done.call(this, dataURL);
          return dataURL;
        } catch (err) {
          options.fail.call(this, errorMsg.clipError);
          throw err;
        }
      }
    }, {
      key: "_resize",
      value: function _resize(width, height) {
        (0, _hideAction["default"])(this._$container, function () {
          this._containerWidth = this._$container.offsetWidth;
          this._containerHeight = this._$container.offsetHeight;
        }, this);
        var size = this._options.size,
            oldClipWidth = this._clipWidth,
            oldClipHeight = this._clipHeight;
        if ((0, _isNumber["default"])(width)) size[0] = width;
        if ((0, _isNumber["default"])(height)) size[1] = height;

        if (this._widthIsPercent || this._heightIsPercent) {
          var ratio = size[0] / size[1];

          if (this._widthIsPercent) {
            this._clipWidth = this._containerWidth / 100 * parseFloat(this._widthIsPercent);

            if (!this._heightIsPercent) {
              this._clipHeight = this._clipWidth / ratio;
            }
          }

          if (this._heightIsPercent) {
            this._clipHeight = this._containerHeight / 100 * parseFloat(this._heightIsPercent);

            if (!this._widthIsPercent) {
              this._clipWidth = this._clipHeight * ratio;
            }
          }
        } else {
          this._clipWidth = size[0];
          this._clipHeight = size[1];
        }

        var clipWidth = this._clipWidth,
            clipHeight = this._clipHeight;
        this._clipSizeRatio = clipWidth / clipHeight;

        if (this._outputWidth && !this._outputHeight) {
          this._outputHeight = this._outputWidth / this._clipSizeRatio;
        }

        if (this._outputHeight && !this._outputWidth) {
          this._outputWidth = this._outputHeight * this._clipSizeRatio;
        }

        (0, _css["default"])(this._$clipLayer, {
          'width': clipWidth,
          'height': clipHeight,
          'margin-left': -clipWidth / 2,
          'margin-top': -clipHeight / 2
        });
        (0, _css["default"])(this._$mask_left, {
          'margin-right': clipWidth / 2,
          'margin-top': -clipHeight / 2,
          'margin-bottom': -clipHeight / 2
        });
        (0, _css["default"])(this._$mask_right, {
          'margin-left': clipWidth / 2,
          'margin-top': -clipHeight / 2,
          'margin-bottom': -clipHeight / 2
        });
        (0, _css["default"])(this._$mask_top, {
          'margin-bottom': clipHeight / 2
        });
        (0, _css["default"])(this._$mask_bottom, {
          'margin-top': clipHeight / 2
        });
        (0, _css["default"])(this._$clip_frame, {
          'width': clipWidth,
          'height': clipHeight
        });
        (0, _css["default"])(this._$clip_frame, prefix + 'transform', 'translate(-50%, -50%)');

        if (clipWidth !== oldClipWidth || clipHeight !== oldClipHeight) {
          this._calcScale();

          this._resizeMoveLayer();

          this._refreshScroll();

          var iScroll = this._iScroll,
              scale = iScroll.scale,
              offsetX = (clipWidth - oldClipWidth) * .5 * scale,
              offsetY = (clipHeight - oldClipHeight) * .5 * scale;
          iScroll.scrollBy(offsetX, offsetY);
        }
      }
      /**
       * 设置截取框的宽高
       * 如果设置了 adaptive 选项，则该方法仅用于修改截取框的宽高比例
       * @param  {Number} width  截取框的宽度
       * @param  {Number} height 截取框的高度
       * @return {PhotoClip}     返回 PhotoClip 的实例对象
       */

    }, {
      key: "size",
      value: function size(width, height) {
        this._resize(width, height);

        return this;
      }
      /**
       * 加载一张图片
       * @param  {String|Object} src 图片的 url，或者图片的 file 文件对象
       * @return {PhotoClip}         返回 PhotoClip 的实例对象
       */

    }, {
      key: "load",
      value: function load(src) {
        this._lrzHandle(src);

        return this;
      }
      /**
       * 清除当前图片
       * @return {PhotoClip}  返回 PhotoClip 的实例对象
       */

    }, {
      key: "clear",
      value: function clear() {
        this._clearImg();

        this._resetScroll();

        if (this._fileList) {
          this._fileList.forEach(function ($file) {
            $file.value = '';
          });
        }

        return this;
      }
      /**
       * 图片旋转到指定角度
       * @param  {Number} angle      可选。旋转的角度
       * @param  {Number} duration   可选。旋转动画的时长，如果为 0 或 false，则表示没有过渡动画
       * @return {PhotoClip|Number}  返回 PhotoClip 的实例对象。如果参数为空，则返回当前的旋转角度
       */

    }, {
      key: "rotate",
      value: function rotate(angle, duration) {
        if (angle === undefined) return this._curAngle;

        this._rotateTo(angle, duration);

        return this;
      }
      /**
       * 图片缩放到指定比例，如果超出缩放范围，则会被缩放到可缩放极限
       * @param  {Number} zoom       可选。缩放比例，取值在 0 - 1 之间
       * @param  {Number} duration   可选。缩放动画的时长，如果为 0 或 false，则表示没有过渡动画
       * @return {PhotoClip|Number}  返回 PhotoClip 的实例对象。如果参数为空，则返回当前的缩放比例
       */

    }, {
      key: "scale",
      value: function scale(zoom, duration) {
        if (zoom === undefined) return this._iScroll.scale;

        this._iScroll.zoom(zoom, undefined, undefined, duration);

        return this;
      }
      /**
       * 截图
       * @return {String}  返回截取后图片的 Base64 字符串
       */

    }, {
      key: "clip",
      value: function clip() {
        return this._clipImg();
      }
      /**
       * 销毁
       * @return {Undefined}  无返回值
       */

    }, {
      key: "destroy",
      value: function destroy() {
        var _this8 = this;

        window.removeEventListener('resize', this._resize);

        this._$container.removeChild(this._$clipLayer);

        this._$container.removeChild(this._$mask);

        (0, _css["default"])(this._$container, this._containerOriginStyle);

        if (this._iScroll) {
          this._iScroll.destroy();
        }

        if (this._hammerManager) {
          this._hammerManager.off('rotatemove');

          this._hammerManager.off('rotateend');

          this._hammerManager.destroy();
        } else {
          this._$moveLayer.removeEventListener('dblclick', this._rotateCW90);
        }

        if (this._$img) {
          this._$img.onload = null;
          this._$img.onerror = null;
        }

        if (this._viewList) {
          this._viewList.forEach(function ($view, i) {
            (0, _css["default"])($view, _this8._viewOriginStyleList[i]);
          });
        }

        if (this._fileList) {
          this._fileList.forEach(function ($file) {
            $file.removeEventListener('change', _this8._fileOnChangeHandle);
            $file.value = null;
          });
        }

        if (this._okList) {
          this._okList.forEach(function ($ok) {
            $ok.removeEventListener('click', _this8._clipImg);
          });
        }

        (0, _destroy2["default"])(this);
      }
    }]);

    return PhotoClip;
  }();

  _exports["default"] = PhotoClip;
  ; // 设置变换注册点

  function setOrigin($obj, originX, originY) {
    originX = (originX || 0).toFixed(2);
    originY = (originY || 0).toFixed(2);
    (0, _css["default"])($obj, prefix + 'transform-origin', originX + 'px ' + originY + 'px');
  } // 设置变换坐标与旋转角度


  function setTransform($obj, x, y, angle) {
    // translate(x, y) 中坐标的小数点位数过多会引发 bug
    // 因此这里需要保留两位小数
    x = x.toFixed(2);
    y = y.toFixed(2);
    angle = angle.toFixed(2);
    (0, _css["default"])($obj, prefix + 'transform', 'translateZ(0) translate(' + x + 'px,' + y + 'px) rotate(' + angle + 'deg)');
  } // 设置变换动画


  function setTransition($obj, x, y, angle, dur, fn) {
    // 这里需要先读取之前设置好的transform样式，强制浏览器将该样式值渲染到元素
    // 否则浏览器可能出于性能考虑，将暂缓样式渲染，等到之后所有样式设置完成后再统一渲染
    // 这样就会导致之前设置的位移也被应用到动画中
    (0, _css["default"])($obj, prefix + 'transform'); // 这里应用的缓动与 iScroll 的默认缓动相同

    (0, _css["default"])($obj, prefix + 'transition', prefix + 'transform ' + dur + 'ms cubic-bezier(0.1, 0.57, 0.1, 1)');
    setTransform($obj, x, y, angle);
    setTimeout(function () {
      (0, _css["default"])($obj, prefix + 'transition', '');
      fn();
    }, dur);
  }

  module.exports = exports.default;
});

/***/ }),
/* 5 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE__5__;

/***/ }),
/* 6 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE__6__;

/***/ }),
/* 7 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE__7__;

/***/ }),
/* 8 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  module.exports = function (context) {
    for (var _len = arguments.length, methods = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
      methods[_key - 1] = arguments[_key];
    }

    methods.forEach(function (method) {
      context[method] = context[method].bind(context);
    });
  };
});

/***/ }),
/* 9 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  module.exports = function (context) {
    // 清除所有属性
    Object.getOwnPropertyNames(context).forEach(function (prop) {
      delete context[prop];
    });
    context.__proto__ = Object.prototype;
  };
});

/***/ }),
/* 10 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  var isArray = __webpack_require__(0);

  var isObject = __webpack_require__(1);

  var isBoolean = __webpack_require__(11);

  var isFunction = __webpack_require__(12);

  var isPlainObject = __webpack_require__(13);

  module.exports = function extend() {
    var options,
        name,
        src,
        copy,
        copyIsArray,
        target = arguments[0] || {},
        toString = Object.prototype.toString,
        i = 1,
        length = arguments.length,
        deep = false; // 处理深拷贝

    if (isBoolean(target)) {
      deep = target; // Skip the boolean and the target

      target = arguments[i] || {};
      i++;
    } // Handle case when target is a string or something (possible in deep copy)


    if (!isObject(target) && !isFunction(target)) {
      target = {};
    } // 如果没有合并的对象，则表示 target 为合并对象，将 target 合并给当前函数的持有者


    if (i === length) {
      target = this;
      i--;
    }

    for (; i < length; i++) {
      // Only deal with non-null/undefined values
      if ((options = arguments[i]) != null) {
        // Extend the base object
        for (name in options) {
          src = target[name];
          copy = options[name]; // 防止死循环

          if (target === copy) {
            continue;
          } // 深拷贝对象或者数组


          if (deep && copy && ((copyIsArray = isArray(copy)) || isPlainObject(copy))) {
            if (copyIsArray) {
              copyIsArray = false;
              src = src && isArray(src) ? src : [];
            } else {
              src = src && isPlainObject(src) ? src : {};
            }

            target[name] = extend(deep, src, copy);
          } else if (copy !== undefined) {
            // 仅忽略未定义的值
            target[name] = copy;
          }
        }
      }
    } // Return the modified object


    return target;
  };
});

/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  // 判断是否为布尔值
  module.exports = function (bool) {
    return typeof bool === 'boolean';
  };
});

/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  // 判断是否为函数
  module.exports = function (func) {
    return typeof func === 'function';
  };
});

/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  // 判断对象是否为纯粹的对象（通过 "{}" 或者 "new Object" 创建的）
  module.exports = function (obj) {
    return Object.prototype.toString.call(obj) === '[object Object]';
  };
});

/***/ }),
/* 14 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  // 判断是否为百分比
  module.exports = function (value) {
    return /%$/.test(value + '');
  };
});

/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

  // 创建元素
  module.exports = function (parentNode, className, id, prop) {
    var elem = document.createElement('DIV');

    if (_typeof(className) === 'object') {
      prop = className;
      className = null;
    }

    if (_typeof(id) === 'object') {
      prop = id;
      id = null;
    }

    if (_typeof(prop) === 'object') {
      for (var p in prop) {
        elem.style[p] = prop[p];
      }
    }

    if (className) elem.className = className;
    if (id) elem.id = id;
    parentNode.appendChild(elem);
    return elem;
  };
});

/***/ }),
/* 16 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  // 移除元素
  module.exports = function (elem) {
    elem.parentNode && elem.parentNode.removeChild(elem);
  };
});

/***/ }),
/* 17 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  // 返回指定属性在当前浏览器中的兼容前缀
  // 如果无需兼容前缀，则返回一个空字符串
  // all 是一个布尔值，如果为 true，则会返回包含前缀的属性名
  module.exports = function (prop, all) {
    var returnProp = all ? prop : '';
    var testElem = document.documentElement;
    if (prop in testElem.style) return returnProp;
    var testProp = prop.charAt(0).toUpperCase() + prop.substr(1),
        prefixs = ['Webkit', 'Moz', 'ms', 'O'];

    for (var i = 0, prefix; prefix = prefixs[i++];) {
      if (prefix + testProp in testElem.style) {
        return '-' + prefix.toLowerCase() + '-' + returnProp;
      }
    }

    return returnProp;
  };
});

/***/ }),
/* 18 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  // 设置样式
  var isObject = __webpack_require__(1);

  var isNumber = __webpack_require__(2);

  var cssNumber = {
    'animationIterationCount': true,
    'columnCount': true,
    'fillOpacity': true,
    'flexGrow': true,
    'flexShrink': true,
    'fontWeight': true,
    'lineHeight': true,
    'opacity': true,
    'order': true,
    'orphans': true,
    'widows': true,
    'zIndex': true,
    'zoom': true
  };

  module.exports = function (elem, prop, value) {
    if (isObject(prop)) {
      for (var p in prop) {
        value = prop[p];
        if (isNumber(value) && !cssNumber[prop]) value += 'px';
        elem.style[p] = value;
      }

      return elem;
    }

    if (value === undefined) {
      return window.getComputedStyle(elem)[prop];
    } else {
      if (isNumber(value) && !cssNumber[prop]) value += 'px';
      elem.style[prop] = value;
      return elem;
    }
  };
});

/***/ }),
/* 19 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

  // 设置属性
  module.exports = function (elem, prop, value) {
    if (_typeof(prop) === 'object') {
      for (var p in prop) {
        elem[p] = prop[p];
      }

      return elem;
    }

    if (value === undefined) {
      return elem[prop];
    } else {
      elem[prop] = value;
      return elem;
    }
  };
});

/***/ }),
/* 20 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

  var toArray = __webpack_require__(21); // 获取元素（IE8及以上浏览器）


  module.exports = function (selector, context) {
    if (selector instanceof HTMLElement) {
      return [selector];
    } else if (_typeof(selector) === 'object' && selector.length) {
      return toArray(selector);
    } else if (!selector || typeof selector !== 'string') {
      return [];
    }

    if (typeof context === 'string') {
      context = document.querySelector(context);
    }

    if (!(context instanceof HTMLElement)) {
      context = document;
    }

    return toArray(context.querySelectorAll(selector));
  };
});

/***/ }),
/* 21 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function () {
  "use strict";

  // 类似数组对象转数组
  module.exports = function (obj) {
    return Array.prototype.map.call(obj, function (n) {
      return n;
    });
  };
});

/***/ }),
/* 22 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;(function (global, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [exports, __webpack_require__(3)], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else { var mod; }
})(this, function (_exports, _hideAction) {
  "use strict";

  Object.defineProperty(_exports, "__esModule", {
    value: true
  });
  _exports.getScale = getScale;
  _exports.pointRotate = pointRotate;
  _exports.angleToRadian = angleToRadian;
  _exports.loaclToLoacl = loaclToLoacl;
  _exports.globalToLoacl = globalToLoacl;
  _hideAction = _interopRequireDefault(_hideAction);

  function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

  // 获取最大缩放比例
  function getScale(w1, h1, w2, h2) {
    var sx = w1 / w2;
    var sy = h1 / h2;
    return sx > sy ? sx : sy;
  } // 计算一个点绕原点旋转后的新坐标


  function pointRotate(point, angle) {
    var radian = angleToRadian(angle),
        sin = Math.sin(radian),
        cos = Math.cos(radian);
    return {
      x: cos * point.x - sin * point.y,
      y: cos * point.y + sin * point.x
    };
  } // 角度转弧度


  function angleToRadian(angle) {
    return angle / 180 * Math.PI;
  } // 计算layerTwo上的x、y坐标在layerOne上的坐标


  function loaclToLoacl(layerOne, layerTwo, x, y) {
    x = x || 0;
    y = y || 0;
    var layerOneRect, layerTwoRect;
    (0, _hideAction["default"])([layerOne, layerTwo], function () {
      layerOneRect = layerOne.getBoundingClientRect();
      layerTwoRect = layerTwo.getBoundingClientRect();
    });
    return {
      x: layerTwoRect.left - layerOneRect.left + x,
      y: layerTwoRect.top - layerOneRect.top + y
    };
  } // 计算相对于窗口的x、y坐标在layer上的坐标


  function globalToLoacl(layer, x, y) {
    x = x || 0;
    y = y || 0;
    var layerRect;
    (0, _hideAction["default"])(layer, function () {
      layerRect = layer.getBoundingClientRect();
    });
    return {
      x: x - layerRect.left,
      y: y - layerRect.top
    };
  }
});

/***/ })
/******/ ]);
});