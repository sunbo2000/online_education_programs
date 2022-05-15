## Progress marker component

Other Languages: [简体中文](https://github.com/aliyunvideo/AliyunPlayer_Web/blob/master/customComponents/src/components/ProgressComponent/README.zh_CN.md)

This component is used to mark the key points of a video. When a user moves the cursor over a marked key point, the specified image is displayed.

### Usage

Reference this component and add the following code to the player configuration:

```js
// progressMarkers indicates the key points marked in the video.
progressMarkers:[{
  offset: 30,
  isCustomized:true,
  coverUrl: 'https://alivc-demo-vod.aliyuncs.com/image/cover/9A3F562E595E4764AD1DD546FA52C6E5-6-2.png',
  title: 'test title',
  describe: 'test string',
}, {
  offset:50,
  isCustomized:true,
  coverUrl: 'https://alivc-demo-vod.aliyuncs.com/image/cover/1E7F402241CD4C0F94AD2BBB5CCC3EC7-6-2.png',
  title: 'test title',
  describe: 'test string',
}, {
  offset:150,
  isCustomized:true,
  coverUrl: 'https://alivc-demo-vod.aliyuncs.com/image/cover/553AEA01161342C8A2B1756E83B69B5B-6-2.png',
  title: 'test title',
  describe: 'test string',
}, {
  offset:120,
  isCustomized:true,
  coverUrl: 'https://alivc-demo-vod.aliyuncs.com/image/cover/553AEA01161342C8A2B1756E83B69B5B-6-2.png',
  title: 'test title',
  describe: 'test string',
}],
components: [{
  name: 'ProgressMarker',
  type: AliPlayerComponent.ProgressMarker
}]
```

`progressMarkers` has the following properties: `title` indicating the title of the key point, `coverUrl` indicating the URL of the image, and `describe` indicating the description of the image.

