package com.PRS.DB;

	
	import java.util.List;
	import org.springframework.data.jpa.repository.JpaRepository;
	import com.PRS.model.LineItem;

	public interface LineItemRepo extends JpaRepository<LineItem, Integer> {
	    static List<LineItem> findByRequestId(Integer requestId) {
		
			return null;
		}
	}
