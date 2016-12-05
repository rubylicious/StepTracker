"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require('@angular/core');
var http_1 = require('@angular/http');
var Observable_1 = require('rxjs/Observable');
require('rxjs/add/operator/map');
require('rxjs/add/operator/do');
require('rxjs/add/operator/catch');
var CalendarService = (function () {
    function CalendarService(_http) {
        this._http = _http;
        this._stepsUrl = 'api/steps/steps.json';
    }
    // getSteps() : string[] {
    // 	return [" ","1000","2000","3000","4000","5000","6000","7000","8000","9000","1000", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""];
    // }
    CalendarService.prototype.getStepsPerMonth = function (date) {
        return this._http.get(this._stepsUrl)
            .map(this.extractData)
            .catch(this.handleError);
        // return this._http.get(this._productUrl).map((response: Response) => <ISteps[]>response.json())
        // .do(data => console.log('All: ' + JSON.stringify(data))).catch(this.handleError);
    };
    CalendarService.prototype.handleError = function (error) {
        console.error(error);
        return Observable_1.Observable.throw(error.json().error || 'Server error');
    };
    CalendarService.prototype.extractData = function (res) {
        var body = res.json();
        return body.data || {};
    };
    CalendarService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], CalendarService);
    return CalendarService;
}());
exports.CalendarService = CalendarService;
//# sourceMappingURL=calendar.service.js.map