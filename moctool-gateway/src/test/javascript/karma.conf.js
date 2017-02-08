// Karma configuration
// http://karma-runner.github.io/0.13/config/configuration-file.html

var sourcePreprocessors = ['coverage'];

function isDebug() {
    return process.argv.indexOf('--debug') >= 0;
}

if (isDebug()) {
    // Disable JS minification if Karma is run with debug option.
    sourcePreprocessors = [];
}

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: 'src/test/javascript/'.replace(/[^/]+/g, '..'),

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            // bower:js
            'src/main/webapp/bower_components/jquery/dist/jquery.js',
            'src/main/webapp/bower_components/messageformat/messageformat.js',
            'src/main/webapp/bower_components/json3/lib/json3.js',
            'src/main/webapp/bower_components/lodash/lodash.js',
            'src/main/webapp/bower_components/jquery-ui/jquery-ui.js',
            'src/main/webapp/bower_components/hamsterjs/hamster.js',
            'src/main/webapp/bower_components/jsPlumb/dist/js/jsPlumb-2.2.6.js',
            'src/main/webapp/bower_components/bootstrap-contextmenu/bootstrap-contextmenu.js',
            'src/main/webapp/bower_components/jQuery-contextMenu/dist/jquery.contextMenu.js',
            'src/main/webapp/bower_components/cytoscape/dist/cytoscape.js',
            'src/main/webapp/bower_components/cytoscape-edgehandles/cytoscape-edgehandles.js',
            'src/main/webapp/bower_components/cytoscape-context-menus/cytoscape-context-menus.js',
            'src/main/webapp/bower_components/cytoscape-cxtmenu/cytoscape-cxtmenu.js',
            'src/main/webapp/bower_components/ev-emitter/ev-emitter.js',
            'src/main/webapp/bower_components/graphlib/dist/graphlib.core.js',
            'src/main/webapp/bower_components/cytoscape-panzoom/cytoscape-panzoom.js',
            'src/main/webapp/bower_components/mark.js/dist/mark.js',
            'src/main/webapp/bower_components/cytoscape-autopan-on-drag/cytoscape-autopan-on-drag.js',
            'src/main/webapp/bower_components/bootstrap/dist/js/bootstrap.js',
            'src/main/webapp/bower_components/angular/angular.js',
            'src/main/webapp/bower_components/angular-aria/angular-aria.js',
            'src/main/webapp/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'src/main/webapp/bower_components/angular-cache-buster/angular-cache-buster.js',
            'src/main/webapp/bower_components/angular-cookies/angular-cookies.js',
            'src/main/webapp/bower_components/angular-dynamic-locale/src/tmhDynamicLocale.js',
            'src/main/webapp/bower_components/ngstorage/ngStorage.js',
            'src/main/webapp/bower_components/angular-loading-bar/build/loading-bar.js',
            'src/main/webapp/bower_components/angular-resource/angular-resource.js',
            'src/main/webapp/bower_components/angular-sanitize/angular-sanitize.js',
            'src/main/webapp/bower_components/angular-translate/angular-translate.js',
            'src/main/webapp/bower_components/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat.js',
            'src/main/webapp/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js',
            'src/main/webapp/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
            'src/main/webapp/bower_components/angular-ui-router/release/angular-ui-router.js',
            'src/main/webapp/bower_components/bootstrap-ui-datetime-picker/dist/datetime-picker.js',
            'src/main/webapp/bower_components/ng-file-upload/ng-file-upload.js',
            'src/main/webapp/bower_components/ngInfiniteScroll/build/ng-infinite-scroll.js',
            'src/main/webapp/bower_components/angular-dragdrop/src/angular-dragdrop.js',
            'src/main/webapp/bower_components/angular-mousewheel/mousewheel.js',
            'src/main/webapp/bower_components/angular-ui-bootstrap/index.js',
            'src/main/webapp/bower_components/imagesloaded/imagesloaded.js',
            'src/main/webapp/bower_components/dagre/dist/dagre.core.js',
            'src/main/webapp/bower_components/dagre/dist/dagre.core.min.js',
            'src/main/webapp/bower_components/angular-toastr/dist/angular-toastr.tpls.js',
            'src/main/webapp/bower_components/angular-animate/angular-animate.js',
            'src/main/webapp/bower_components/bootstrap-tour/build/js/bootstrap-tour.js',
            'src/main/webapp/bower_components/bootstrap-tour/build/js/bootstrap-tour-standalone.js',
            'src/main/webapp/bower_components/angular-mocks/angular-mocks.js',
            'src/main/webapp/bower_components/angular-pan-zoom/release/panzoom.min.js',
            'src/main/webapp/bower_components/qtip2/jquery.qtip.js',
            'src/main/webapp/bower_components/qtip2/basic/jquery.qtip.js',
            'src/main/webapp/bower_components/cytoscape-dagre/cytoscape-dagre.js',
            'src/main/webapp/bower_components/cytoscape-qtip/cytoscape-qtip.js',
            // endbower
            'src/main/webapp/app/app.module.js',
            'src/main/webapp/app/app.state.js',
            'src/main/webapp/app/app.constants.js',
            'src/main/webapp/app/**/*.+(js|html)',
            'src/test/javascript/spec/helpers/module.js',
            'src/test/javascript/spec/helpers/httpBackend.js',
            'src/test/javascript/**/!(karma.conf|protractor.conf).js'
        ],


        // list of files / patterns to exclude
        exclude: ['src/test/javascript/e2e/**'],

        preprocessors: {
            './**/*.js': sourcePreprocessors
        },

        reporters: ['dots', 'junit', 'coverage', 'progress'],

        junitReporter: {
            outputFile: '../target/test-results/karma/TESTS-results.xml'
        },

        coverageReporter: {
            dir: 'target/test-results/coverage',
            reporters: [
                {type: 'lcov', subdir: 'report-lcov'}
            ]
        },

        // web server port
        port: 9876,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['PhantomJS'],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        // to avoid DISCONNECTED messages when connecting to slow virtual machines
        browserDisconnectTimeout: 10000, // default 2000
        browserDisconnectTolerance: 1, // default 0
        browserNoActivityTimeout: 4 * 60 * 1000 //default 10000
    });
};
