# android-DevFileDialog
Library to select file from the file system for android
# Example
```
DevFileDialog dialog_file = new DevFileDialog(getApplication);
dialog_file.setOpenDialogListener(new DevFileDialog.DevFileDialogListener() {
  @Override
  public void OnSelectedFile(String filePath,String fileName) {
    Toast.makeText(getApplicationContext(),"Name file: " + fileName + "; Path file: " + filePath,Toast.LENGTH_SHORT).show();
  }
});
dialog_file.show();
```
