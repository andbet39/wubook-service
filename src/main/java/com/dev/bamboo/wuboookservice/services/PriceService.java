package com.dev.bamboo.wuboookservice.services;

import com.dev.bamboo.wuboookservice.domains.AggregatedPriceInfoResult;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class PriceService {

    public HashMap<Date,Float> calculateDiffs(List<AggregatedPriceInfoResult> bamboo , List<AggregatedPriceInfoResult> other)
    {
        int i=0;
        HashMap<Date,Float> diffs = new HashMap<>();

        for(AggregatedPriceInfoResult o: other){
            AggregatedPriceInfoResult b = bamboo.get(i);

            if (o.getDate().equals(b.getDate())){
                Float diff= b.getMin() - o.getMin();
                diffs.put(b.getDate(),diff);
                i++;

            }else{
                diffs.put(o.getDate(),0.0F);
            }
        }

        return diffs;
    }
}
