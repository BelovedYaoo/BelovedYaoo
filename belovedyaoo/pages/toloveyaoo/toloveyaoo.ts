import {
  getCurrWeekList,
  formateDate
} from './util'
Page({
  data: {
    startTime: new Date('2024-09-02T00:00:00').getTime() as number,
    currentWeek: 1 as number,
    time: {
      one: [{
        index: 1,
        timeStart: '08:00',
        timeEnd: '08:45'
      },
      {
        index: 2,
        timeStart: '08:55',
        timeEnd: '09:40'
      },
      {
        index: 3,
        timeStart: '10:00',
        timeEnd: '10:45'
      },
      {
        index: 4,
        timeStart: '10:55',
        timeEnd: '11:40'
      }
      ],
      two: [{
        index: 5,
        timeStart: '14:30',
        timeEnd: '15:15'
      },
      {
        index: 6,
        timeStart: '15:25',
        timeEnd: '16:10'
      },
      ],
      three: [{
        index: 7,
        timeStart: '16:30',
        timeEnd: '17:15'
      },
      {
        index: 8,
        timeStart: '17:25',
        timeEnd: '18:10'
      },
      ]
    },
    schedule: {
      one: [{
        sub: '软件项目管理',
        add: '6-13',
        tec: "T2-D101",
        color: '#fad0c4',
        type: 1,
      }, {
        sub: 'UML\n建模',
        add: '6-13',
        tec: "T2-D101",
        color: '#c1cbd7',
        type: 1,
      }, {
        sub: '软件项目管理',
        add: '6-13',
        tec: "T2-D101",
        color: '#965454',
        type: 1,
      }, {
        sub: '',
        add: '',
        tec: "",
        color: '',
        type: 0,
      },
      {
        sub: 'UML\n建模实验',
        add: '9-13',
        tec: "P-D706",
        color: '#C9C0D3',
        type: 0,
      },
      ],
      two: [{
        sub: 'UML\n建模',
        add: '6-13',
        tec: "T2-D101",
        color: '#A29988',
        type: 1,
      },
      {
        sub: '软件质量保证与测试',
        add: '2-13',
        tec: "T2-D101",
        color: '#fda085',
        type: 1,
      },
      {
        sub: '软件项目管理实验',
        add: '7-14',
        tec: "P-D706",
        color: '#fbc2eb',
        type: 1,
      }, {
        sub: '软件质量保证与测试',
        add: '1-12',
        tec: "T2-D101",
        color: '#6B5152',
        type: 1,
      },
      {
        sub: '算法分析与设计',
        add: '6-13',
        tec: "T2-D101",
        color: 'pink',
        type: 1, //0-无  1-有
      },
      ],
      three: [{
        sub: '移动开发技术基础',
        add: '2-4',
        tec: "T2-D101",
        color: '#7b8b6f',
        type: 0,
      }, {
        sub: '移动开发技术基础',
        add: '2-4',
        tec: "T2-D101",
        color: '#7b8b6f',
        type: 0,
      }, {
        sub: '算法分析与设计',
        add: '6-13',
        tec: "T2-D101",
        color: 'pink',
        type: 1, //0-无  1-有
      },
      {
        sub: '移动开发技术基础',
        add: '2-4',
        tec: "T2-D101",
        color: '#7b8b6f',
        type: 0,
      },
      {
        sub: '移动开发技术基础实验',
        add: '等待排课',
        tec: "P-D602",
        color: '#7b8b6f',
        type: 0,
      },
      ],
      four: [{
        sub: '移动开发技术基础',
        add: '2-4',
        tec: "T2-D101",
        color: '#7b8b6f',
        type: 0,
      }, {
        sub: '移动开发技术基础',
        add: '2-4',
        tec: "T2-D101",
        color: '#7b8b6f',
        type: 0,
      }, {
        sub: '',
        add: '',
        tec: "",
        color: '',
        type: 0,
      },
      {
        sub: '移动开发技术基础',
        add: '2-4',
        tec: "T2-D101",
        color: '#7b8b6f',
        type: 0,
      },
      ]
    },
    weekList: [],
    isShow: false,
    current: {},
  },
  getDetail(e: any) {
    let {
      item
    } = e.currentTarget.dataset;
    console.log(item)
    this.setData({
      current: item,
      isShow: true
    })
  },
  close() {
    this.setData({
      isShow: false
    })
  },
  onShow() {
    if (typeof this.getTabBar == 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 2
      });
    }
    let time = new Date(),
      list = getCurrWeekList(time),
      weekList = [] as any
    list.forEach(item => {
      weekList.push({
        day: [item.split('-')[1], item.split('-')[2]].join('-'),
        week: "星期" + "日一二三四五六".charAt((new Date(item)).getDay()),
        isCurr: formateDate(time) == item
      })
    });
    this.setData({
      weekList,
    })
  },
  onLoad: function (): void {
    var diffInMs = Math.abs(new Date().getTime() - this.data.startTime);
    var diffInWeeks = diffInMs / (1000 * 60 * 60 * 24 * 7);
    this.setData({
      currentWeek: Math.ceil(diffInWeeks)
    });
  }
})