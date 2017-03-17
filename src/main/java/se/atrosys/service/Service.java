package se.atrosys.service;

import io.reactivex.Observable;

/**
 * TODO write documentation
 */
public interface Service {
	Observable<String> getString(Integer id);
	Integer times(Integer id);
}
