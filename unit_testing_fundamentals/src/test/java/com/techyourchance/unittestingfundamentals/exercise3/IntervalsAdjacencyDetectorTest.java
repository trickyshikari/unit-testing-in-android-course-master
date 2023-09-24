package com.techyourchance.unittestingfundamentals.exercise3;

import static org.hamcrest.CoreMatchers.is;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setup(){
        SUT = new IntervalsAdjacencyDetector();
    }

    @Test
    public void isAdjacent_1(){//первый слева второй справа
        Interval inter1 = new Interval(1,2),
                 inter2 = new Interval(3,4);
        boolean result = SUT.isAdjacent(inter1,inter2);
        Assert.assertThat(result,is(false));
    }

    @Test
    public void isAdjacent_2(){//первый справа второй слева
        Interval inter1 = new Interval(3,4),
                inter2 = new Interval(1,2);
        boolean result = SUT.isAdjacent(inter1,inter2);
        Assert.assertThat(result,is(false));
    }

    @Test
    public void isAdjacent_3(){//пересекаются
        Interval inter1 = new Interval(1,3),
                inter2 = new Interval(2,4);
        boolean result = SUT.isAdjacent(inter1,inter2);
        Assert.assertThat(result,is(false));
    }

    @Test
    public void isAdjacent_4(){//смежные начало первого и конец второго
        Interval inter1 = new Interval(3,4),
                inter2 = new Interval(1,3);
        boolean result = SUT.isAdjacent(inter1,inter2);
        Assert.assertThat(result,is(true));
    }

    @Test
    public void isAdjacent_5(){//смежные начало второго и конец первого
        Interval inter1 = new Interval(1,2),
                inter2 = new Interval(2,4);
        boolean result = SUT.isAdjacent(inter1,inter2);
        Assert.assertThat(result,is(true));
    }

    @Test
    public void isAdjacent_6(){//интервалы одинаковые
        Interval inter1 = new Interval(1,2),
                inter2 = new Interval(1,2);
        boolean result = SUT.isAdjacent(inter1,inter2);
        Assert.assertThat(result,is(false));
    }


}