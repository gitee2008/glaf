package com.glaf.heathcare.mapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.glaf.heathcare.domain.*;
import com.glaf.heathcare.query.*;

/**
 * 
 * Mapper接口
 *
 */

@Component("com.glaf.heathcare.mapper.TriphopathiaMapper")
public interface TriphopathiaMapper {

	void bulkInsertTriphopathia(List<Triphopathia> list);

	void bulkInsertTriphopathia_oracle(List<Triphopathia> list);

	void deleteTriphopathiaById(Long id);

	Triphopathia getTriphopathiaById(Long id);

	int getTriphopathiaCount(TriphopathiaQuery query);

	List<Triphopathia> getTriphopathias(TriphopathiaQuery query);

	void insertTriphopathia(Triphopathia model);

	void updateTriphopathia(Triphopathia model);

}
