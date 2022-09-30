package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.annotation.KqFiled;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.domain.*;
import cc.mrbird.febs.system.dao.KqHzMapper;
import cc.mrbird.febs.system.service.IEmolumentService;
import cc.mrbird.febs.system.service.IKqHzService;
import cc.mrbird.febs.system.service.IKqPeriodService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Service
public class KqHzServiceImpl extends ServiceImpl<KqHzMapper, KqHz> implements IKqHzService {
    @Autowired
    IKqPeriodService iKqPeriodService;

    @Autowired
    IEmolumentService iEmolumentService;
    @Transactional
    public void jkToHzList(List<KqJk> kqJks, Date period) throws IllegalAccessException {
        List<KqHz>kqHzList=new ArrayList<>();
        int dayOfMonth=DateUtil.getDayOfMonth(period);
        for (KqJk kqJk:kqJks){
            int bing=0,shi=0,chan=0,xiuxi=0;
            KqHz kqHz=new KqHz();
            Field[] fields=KqJk.class.getDeclaredFields();
            int i=0;
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.getAnnotation(KqFiled.class)!=null&&i<dayOfMonth){
                    String s= (String) field.get(kqJk);
                    if(s!=null){
                        if(s.equals("630")){
                            bing++;
                        }else if(s.equals("640")){
                            chan++;
                        }else if(s.equals("740")){
                            shi++;
                        }
                    }else {
                        xiuxi++;
                    }
                    i++;
                }
            }
            kqHz.setKqstatus(kqJk.getId()==null?0:1);
            kqHz.setIdnumber(kqJk.getIdnumber());
            kqHz.setEmployeecode(kqJk.getEmployeecode());
            kqHz.setEmployeetype(kqJk.getEmployeetype());
            kqHz.setEmployeename(kqJk.getEmployeename());
            kqHz.setKong(dayOfMonth-shi-bing-xiuxi);
            kqHz.setXx(xiuxi);
            kqHz.setKqdate(period);
            kqHz.setDept(kqJk.getKqdept());
            kqHz.setBing(bing);
            kqHz.setShi(shi);
            kqHz.setChan(chan);
            kqHzList.add(kqHz);
        }
        List<KqHz>cfList= getDuplicateElements(kqHzList, kqHz -> kqHz.getIdnumber());
        kqHzList.removeAll(cfList);
        for (int m = 0; m <cfList.size(); m++) {
            KqHz target=cfList.get(m);
            for (int n = m+1; n <cfList.size(); n++) {
                KqHz source=cfList.get(n);
                if(target.getIdnumber().equals(source.getIdnumber())){
                    lj(target,source);
                    KqHz k=cfList.get(m);
                    k.setKong(dayOfMonth-k.getShi()-k.getBing()-k.getXx());
                    cfList.remove(n);
                    n--;
                }
            }
        }
        kqHzList.addAll(cfList);
        this.baseMapper.deleteByKqdate(period);
        saveBatch(kqHzList);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(period);

        this.baseMapper.setKK(period,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    // 方法
    public static <E, R> List<E> getDuplicateElements(List<E> list, Function<E, R> function) {
        Map<R, List<E>> collect = list.stream().collect(Collectors.groupingBy(function));
        return collect.entrySet().stream().filter(entry -> entry.getValue().size() > 1).flatMap(entry -> entry.getValue().stream()).collect(Collectors.toList());
    }

    public static void lj(KqHz target,KqHz source) throws IllegalAccessException {
        Field[] fields=KqHz.class.getDeclaredFields();
        for(Field f:fields){
            f.setAccessible(true);
            if(f.getAnnotation(KqFiled.class)!=null){
                Integer t= (Integer) f.get(target);
                Integer s=(Integer) f.get(source);
                if(s!=null&&t!=null){
                    f.set(target,s+t);
                }
            }
        }
    }
    public IPage<KkList> getKkList(QueryRequest request, KqHz kqHz){
        Page<Emolument> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getKkJkList(page,kqHz);
    }

    public List<KkList> getKkList( KqHz kqHz){
        return this.baseMapper.getKkJkList(kqHz);
    }

    public IPage<KqHz> getHzReport(QueryRequest request, KqHz kqHz){
        Page<Emolument> page = new Page<>();
        SortUtil.handlePageSort(request, page, false);
        return this.baseMapper.getHzReport(page,kqHz);
    }

    public List<KqHz> getHzReport( KqHz kqHz){
        return this.baseMapper.getHzReport(kqHz);
    }

    public KqHz getHzSum(KqHz kqHz) {
        return this.baseMapper.getHzSum(kqHz);
    }

    public void updateKqHz(KqHz kqHz){
        updateById(kqHz);
    }

    public KqHz getSum(KqHz kqHz){
        return this.baseMapper.getSum(kqHz);
    }

    public void passKqHz(KqPeriod kqPeriod){
        kqPeriod.setState(0);
        iKqPeriodService.updatePeriod(kqPeriod);
        kqPeriod.setPeriod(DateUtil.getNextMonth(kqPeriod.getPeriod()));
        kqPeriod.setId(null);
        iKqPeriodService.createPeriod(kqPeriod);
    }

    public KqHz getByIdnumberAndPeriod(String idnumber,Date period){
        LambdaQueryWrapper<KqHz> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KqHz::getIdnumber, idnumber);
        queryWrapper.eq(KqHz::getKqdate,period);
        return baseMapper.selectOne(queryWrapper);
    }

}
