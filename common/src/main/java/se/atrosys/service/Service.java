package se.atrosys.service;

import io.reactivex.Observable;
import se.atrosys.model.Model;

/**
 * TODO write documentation
 */
public interface Service {
	Observable<String> getString(Integer id);
	Integer times(Integer id);
	Observable<Model> getModel(Integer id);
	Integer modelTimes(Integer id);
	Model actuallyGetModel(final Integer id);
}
