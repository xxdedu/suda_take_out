package com.xia.suda.dto;

import com.xia.suda.entity.Setmeal;
import com.xia.suda.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
