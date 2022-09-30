package cc.mrbird.febs.common.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * FEBS常量
 */
public class FebsConstant {

    // user缓存前缀
    public static final String USER_CACHE_PREFIX = "febs.cache.user.";
    // user角色缓存前缀
    public static final String USER_ROLE_CACHE_PREFIX = "febs.cache.user.role.";
    // user权限缓存前缀
    public static final String USER_PERMISSION_CACHE_PREFIX = "febs.cache.user.permission.";
    // user个性化配置前缀
    public static final String USER_CONFIG_CACHE_PREFIX = "febs.cache.user.config.";
    // token缓存前缀
    public static final String TOKEN_CACHE_PREFIX = "febs.cache.token.";

    // 存储在线用户的 zset前缀
    public static final String ACTIVE_USERS_ZSET_PREFIX = "febs.user.active";

    // 排序规则： descend 降序
    public static final String ORDER_DESC = "descend";
    // 排序规则： ascend 升序
    public static final String ORDER_ASC = "ascend";

    // 按钮
    public static final String TYPE_BUTTON = "1";
    // 菜单
    public static final String TYPE_MENU = "0";

    //特殊科室ID
    //手术室
    public static final String SPECIAL_DEPT_SSS="1028";
    //重症护士
    public static final String SPECIAL_DEPT_ZZHS="1060";
    //消毒供应室
    public static final String SPECIAL_DEPT_XDGYZX="1063";
    //急诊科护士
    public static final String SPECIAL_DEPT_JZKHS="1068";
    //血透中心
    public static final String SPECIAL_DEPT_XTZX="1071";
    //介入诊疗中心
    public static final String SPECIAL_DEPT_JRZLZX="1073";
    //内镜中心
    public static final String SPECIAL_DEPT_NJZX="1081";
    //感染科住院部
    public static final String SPECIAL_DEPT_GRKZYB="1089";
    //感染科发热门诊
    public static final String SPECIAL_DEPT_GRKFRMZ="1090";

    //班种启用状态
    public static final Integer PB_STATUS_TY=0;

    public static final Integer PB_STATUS_QY=1;

    public static final List<String> SPECIAL_DEPT_IDS=new ArrayList<String>(){
        {
            add(SPECIAL_DEPT_SSS);
            add(SPECIAL_DEPT_ZZHS);
            add(SPECIAL_DEPT_XDGYZX);
            add(SPECIAL_DEPT_JZKHS);
            add(SPECIAL_DEPT_XTZX);
            add(SPECIAL_DEPT_JRZLZX);
            add(SPECIAL_DEPT_NJZX);
        }
    };


    //新增提示
    public static final String INSERT_SUCCESS="新增成功";
    //修改提示
    public static final String UPDATE_SUCCESS="修改成功";
    //删除提示
    public static final String DELETE_SUCCESS="删除成功";

    //审核提示
    public static final String CHECK_SUCCESS="审核通过";

    //OA数据操作类型: 新增
    public static final String OPERATION_INSERT="I";
    //OA数据操作类型: 更新
    public static final String OPERATION_UPDATE="U";

    //中间表DataSource名称
    //财务中间表
    public static final String CW_DATA_SOURCE="cwview";
    //OA同步数据中间表
    public static final String OA_TO_HR_DATA_SOURCE ="oaview";
    //hr同步给OA数据中间表
    public static final String HR_TO_OA_DATA_SOURCE ="oahr";

    //科室编号长度
    public static final int KESHI_LENGTH=3;
    //病区编号长度
    public static final int BINGQU_LENGTH=5;

    //科室等级
    public static final String DEPARTMENT_DJ_BINGQU="3";
    public static final String DEPARTMENT_DJ_KESHI="2";
    public static final String DEPARTMENT_DJ_KESHILEIXING="1";



    public static final String SB_TYPE_ZENG="新增";
    public static final String SB_TYPE_JIAN="减少";
    public static final String SB_TYPE_BU="补缴";

    public static final Integer SB_YANGLAO=1;
    public static final Integer SB_YL=2;
    public static final Integer SB_SY=3;
    public static final Integer SB_GS=4;
    public static final Integer SB_SHENGYU=5;
    public static final Integer SB_DEYL=6;
    public static final Integer SB_BZYL=7;

    // 网络资源 Url
    public static final String MEIZU_WEATHER_URL = "http://aider.meizu.com/app/weather/listWeather";
    public static final String MRYW_TODAY_URL = "https://interface.meiriyiwen.com/article/today";
    public static final String MRYW_DAY_URL = "https://interface.meiriyiwen.com/article/day";
    public static final String TIME_MOVIE_HOT_URL = "https://api-m.mtime.cn/Showtime/LocationMovies.api";
    public static final String TIME_MOVIE_DETAIL_URL = "https://ticket-api-m.mtime.cn/movie/detail.api";
    public static final String TIME_MOVIE_COMING_URL = "https://api-m.mtime.cn/Movie/MovieComingNew.api";
    public static final String TIME_MOVIE_COMMENTS_URL = "https://ticket-api-m.mtime.cn/movie/hotComment.api";

}
