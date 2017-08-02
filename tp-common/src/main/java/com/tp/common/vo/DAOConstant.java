package com.tp.common.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

/**
 * 存放公共常量
 * @author szy
 *
 */
public final class DAOConstant {
	public static final String DATA_SOURCE_KEY = "dataSourceKey";
	public enum MYBATIS_SPECIAL_STRING{
		ORDER_BY,LIMIT,COLUMNS,TABLES,WHERE,LIKE,INLIST,GT,LT;
		public static List<String> list(){
			List<String> result = new ArrayList<String>();
			for (MYBATIS_SPECIAL_STRING entry : MYBATIS_SPECIAL_STRING.values()) {
				result.add(entry.name());
			}
			return result;
		}
	}

	public static final Integer DEFUALT_SIZE = 10;
	public static final Integer DEFUALT_PAGE=1;
	
	public static class WHERE_ENTRY implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5213190348261488762L;
		public MYBATIS_SPECIAL_STRING modelType;
		public Object condition;
		public String column;
		public WHERE_ENTRY(String column,MYBATIS_SPECIAL_STRING modelType,Object condition){
			this.column = column;
			this.modelType = modelType;
			this.condition = condition;
		}
		public String toString(){
			if(modelType!=null && condition!=null && column!=null){
				if(MYBATIS_SPECIAL_STRING.LIKE.equals(modelType) && condition instanceof String){
					return column+" like concat('%','"+condition+"','%')";
				}else if(MYBATIS_SPECIAL_STRING.INLIST.equals(modelType) && condition instanceof Collection){
					if((CollectionUtils.isNotEmpty((Collection<?>)condition))){
						return column+" in ("+StringUtil.join((Collection<?>)condition, Constant.SPLIT_SIGN.COMMA)+")";
					}
				}else if(MYBATIS_SPECIAL_STRING.INLIST.equals(modelType) && condition instanceof Object[]){
					if(((Object[])condition)!=null && ((Object[])condition).length>0){
						return column+" in ("+StringUtil.join((Object[])condition, Constant.SPLIT_SIGN.COMMA)+")";
					}
				}else if(MYBATIS_SPECIAL_STRING.GT.equals(modelType)){
					if(condition instanceof Number){
						return column+" >= "+condition;
					}else if(condition instanceof Date){
						return column+" >= '"+DateUtil.formatDateTime((Date)condition)+"'";
					}
				}else if(MYBATIS_SPECIAL_STRING.LT.equals(modelType)){
					if(condition instanceof Number){
						return column+" <= "+condition;
					}else if(condition instanceof Date){
						return column+" <= '"+DateUtil.formatDateTime((Date)condition)+"'";
					}
				}
			}
			return "";
		}
	}
	
}
