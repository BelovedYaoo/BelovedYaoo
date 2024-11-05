
const classMarkers = [
  {
    longitude: 111.67080333747128,
    latitude: 29.04441462760814,
    labelContent: '第一教学楼(T1)'
  },
  {
    longitude: 111.67006561557582,
    latitude: 29.044941814559998,
    labelContent: '第二教学楼(T2)'
  }
] as Array<any>;

const officeMarkers = [
  {
    longitude: 111.67136227045319,
    latitude: 29.04495732591908,
    labelContent: '第二办公楼'
  },
  { latitude: 29.044062875568187,
    longitude: 111.67073480598356,
    labelContent: '船型楼'
   }
] as Array<any>;

const labMarkers = [
  {latitude: 29.052327468289544, longitude: 111.67443888289915,labelContent: '综合实习实训大楼(P)'},
  {latitude: 29.043512666282002, longitude: 111.67097580215886,labelContent: '第一实验楼'}
] as Array<any>

const dormMarkers = [
  {latitude: 29.05238674278106, longitude: 111.66812217365339,labelContent: '10舍'},
  {latitude: 29.052418210557523, longitude: 111.66889719192,labelContent: '11舍'},
  {latitude: 29.04352617841001, longitude: 111.672697905924,labelContent: '17舍'}
] as Array<any>

function generateList(startIndex: number, originList: AnyArray, iconPath: string): AnyArray {
  let markerList = [] as AnyArray;
  let index = startIndex;
  originList.forEach(marker => {
    markerList.push({
      id: index,
      width: 20,
      height: 20,
      longitude: marker.longitude,
      latitude: marker.latitude,
      iconPath: iconPath,
      label: {
        content: marker.labelContent,
        textAlign: 'center',
      },
    });
    index++;
  });
  console.log(markerList);
  return markerList;
}

export const markers = [
  ...generateList(1000, classMarkers, './images/jiaoxue.png'),
  ...generateList(2000, officeMarkers, './images/bangong.png'),
  ...generateList(3000, labMarkers, './images/shiyan.png'),
  ...generateList(4000, dormMarkers, './images/chuang.png'),
] as Array<any>;
