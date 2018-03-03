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

@Component("com.glaf.heathcare.mapper.TriphopathiaItemMapper")
public interface TriphopathiaItemMapper {

	void bulkInsertTriphopathiaItem(List<TriphopathiaItem> list);

	void bulkInsertTriphopathiaItem_oracle(List<TriphopathiaItem> list);

	void deleteTriphopathiaItemById(Long id);

	TriphopathiaItem getTriphopathiaItemById(Long id);

	int getTriphopathiaItemCount(TriphopathiaItemQuery query);

	List<TriphopathiaItem> getTriphopathiaItems(TriphopathiaItemQuery query);

	void insertTriphopathiaItem(TriphopathiaItem model);

	void updateTriphopathiaItem(TriphopathiaItem model);

}
