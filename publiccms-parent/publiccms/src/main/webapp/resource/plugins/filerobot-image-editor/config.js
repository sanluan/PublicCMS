var filerobotImageEditorConfig = {
  closeAfterSave: true,
  avoidChangesNotSavedAlertOnLeave:true,
  annotationsCommon: {
    fill: "#ff0000",
  },
  useBackendTranslations:false,
  Text: { text: "..." },
  Rotate: { angle: 90, componentType: "slider" },
  tabsIds: ["Adjust","Annotate","Filters","Finetune","Watermark"],//,"Resize"
  defaultTabId: "Adjust",
  defaultToolId: "Crop"
};