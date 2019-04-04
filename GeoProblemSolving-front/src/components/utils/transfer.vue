<template>
    <Transfer
        :data="data1"
        :target-keys="targetKeys1"
        :render-format="render1"
        @on-change="handleChange1"
        :list-style="listStyle"
        :not-found-text="hintText"
        :titles="title"></Transfer>
</template>
<script>
    export default {
        data () {
            return {
                //动态获取，这种写法值得借鉴
                data1: this.getMockData(),
                targetKeys1: this.getTargetKeys(),
                listStyle:{
                  width:'300px',
                  height:'300px',
                },
                hintText:"please arrange the work",
                title:['All members','Selection members']
            }
        },
        methods: {

            //这里去后台获取全部group成员的信息，做一个遍历，添加以下属性，索引用i确定，标签则用name命名，
            //description可以加入成员的属性或者特长，便于筛选

            //该函数的作用是创建一个符合tarnsfer格式的数组并返回
            //成功以后会向后台发送修改请求，同时前台重新渲染，将成员列表信息进行绑定，一旦有变化会重新渲染。
            //使用mounted来控制
            getMockData () {
                let mockData = [];
                for (let i = 1; i <= 20; i++) {
                    mockData.push({
                        key: i.toString(),
                        label: 'Content ' + i,
                        description: 'The desc of content  ' + i,
                        // disabled: Math.random() * 3 < 1
                    });
                }
                return mockData;
            },
            getTargetKeys () {
                return this.getMockData()
                        .filter(() => Math.random() * 2 > 1)
                        .map(item => item.key);
            },
            render1 (item) {
                return item.label + '(' + item.description + ')';
            },
            handleChange1 (newTargetKeys, direction, moveKeys) {
                 
                 
                 
                this.targetKeys1 = newTargetKeys;
            }
        }
    }
</script>
