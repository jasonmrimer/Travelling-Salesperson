import { shallow, ShallowWrapper } from 'enzyme';
import React from 'react';
import { RouteContainer } from './RouteContainer';
import { RouteInfo } from './RouteInfo';
import { RouteModel } from '../models/RouteModel';
import { StyledMapInput } from './MapInput';

describe('RouteContainer', () => {
  let routeContainer: ShallowWrapper;
  let weightedRoute: RouteModel;
  let getNewRoute = () => {
    return null;
  };

  beforeEach(() => {
    weightedRoute = new RouteModel();

    routeContainer = shallow(
      <RouteContainer
        getStaticRoute={() => {
        }}
        getNewRoute={getNewRoute}
        weightedRoute={weightedRoute}
        loading={false}/>
    );
  });

  it('should have a div', () => {
    expect(routeContainer.find('div').exists()).toBeTruthy();
  });

  it('should setup and display weightedRoute info', () => {
    expect(routeContainer.find(RouteInfo).props().weightedRoute).toBe(weightedRoute);
    expect(routeContainer.find(RouteInfo).exists()).toBeTruthy();
  });

  it('should display loading info', () => {
    expect(routeContainer.find('.loading').exists()).toBeFalsy();
    routeContainer = shallow(
      <RouteContainer
        getStaticRoute={() => {
        }}
        getNewRoute={getNewRoute}
        weightedRoute={weightedRoute}
        loading={true}
      />
    );
    expect(routeContainer.find('.loading').exists()).toBeTruthy();
  });

  it('should display and facilitate map input', () => {
    expect(routeContainer.find(StyledMapInput).exists()).toBeTruthy();
    expect(routeContainer.find(StyledMapInput).prop('getNewRoute')).toBe(getNewRoute);
  });
});