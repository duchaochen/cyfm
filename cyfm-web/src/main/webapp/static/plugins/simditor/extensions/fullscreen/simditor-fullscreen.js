(function() {
  var __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  (function(factory) {
    if ((typeof define === 'function') && define.amd) {
      return define(['simditor'], factory);
    } else {
      return factory(window.Simditor);
    }
  })(function(Simditor) {
    var FullScreenButton;
    FullScreenButton = (function(_super) {
      __extends(FullScreenButton, _super);

      function FullScreenButton() {
        this.isExpand = false;
        FullScreenButton.__super__.constructor.apply(this, arguments);
      }

      FullScreenButton.prototype._init = function() {
        FullScreenButton.__super__._init.apply(this, arguments);
        return this.setIcon("expand");
      };

      FullScreenButton.prototype.name = 'fullscreen';

      FullScreenButton.prototype.title = 'full-screen';

      FullScreenButton.prototype.saveStatus = function() {
        var body, el, toolbar, wrapper;
        el = this.editor.el;
        wrapper = this.editor.wrapper;
        toolbar = this.editor.toolbar;
        body = this.editor.body;
        return this.cssStatus = {
          el: {
            position: el.css('position'),
            left: el.css('left'),
            right: el.css('right'),
            top: el.css('top'),
            bottom: el.css('bottom')
          },
          wrapper: wrapper.css("height"),
          toolbar: {
            wrapper: {
              width: toolbar.wrapper.css("width")
            }
          },
          body: {
            maxHeight: body.css("maxHeight"),
            overflow: body.css("overflow")
          }
        };
      };

      FullScreenButton.prototype.setIcon = function(icon) {
        return this.el.find("span").removeClass().addClass("fa fa-" + icon);
      };

      FullScreenButton.prototype.resetStatus = function() {
        this.editor.el.css(this.cssStatus.el);
        this.editor.wrapper.css(this.cssStatus.wrapper);
        this.editor.toolbar.wrapper.css(this.cssStatus.toolbar.wrapper);
        return this.editor.body.css(this.cssStatus.body);
      };

      FullScreenButton.prototype.doFullScreen = function() {
        var toolbarHeight, wrapperHeight;
        this.editor.el.css('position', 'fixed').css('left', "9px").css('right', "9px").css('top', "9px").css('bottom', "9px");
        this.editor.wrapper.css("height", "100%");
        this.editor.toolbar.wrapper.css('width', "100%");
        toolbarHeight = this.editor.toolbar.wrapper.height();
        wrapperHeight = this.editor.wrapper.height();
        return this.editor.body.css("maxHeight", wrapperHeight - toolbarHeight - 70 + "px").css("overflow", "auto");
      };

      FullScreenButton.prototype.command = function() {
        if (this.isExpand) {
          this.setIcon('expand');
          this.resetStatus();
          this.isExpand = false;
          return;
        }
        this.setIcon('compress');
        this.saveStatus();
        this.isExpand = true;
        return this.doFullScreen();
      };

      return FullScreenButton;

    })(Simditor.Button);
    return Simditor.Toolbar.addButton(FullScreenButton);
  });

}).call(this);
