function Car(options){
  this.title = options.title;
  this.drivestop2 = function(){
    return "here"
  }
}
Car.prototype.drive = function(){
  return "Vroom";
}
Car.prototype.drivestop = function(){
  return "Vroom end";
}
const car = new Car({title:"BMW"});
console.log(car);
console.log(car.drive());


function getExcelKeys(){
  let row = this.totalForm.currentList[0];
  for(let key in row){
    this.excelKey.push({
      key,
      value:row[key]
    });
  }
  console.log('this.excelKey===>',this.excelKey);
  this.excelKey.splice(0,1);
}

//获取当前表头对应的列
function getCurrentKey(name){
  let key;
  this.excelKey.forEach(item => {
    if(item.value === name){
      key = item.key;
    }
  });
  return key;
}

function getTotalMoney(){
  //遍历数组，但是只是针对指定列的元素项进行操作
  let totalMoney = 0 ; //作为统计和的变量
  let key = this.getCurrentKey("金额");
  this.excelKey.forEach(item=>{
    totalMoney = totalMoney + item[key];
  });
  this.totalMoney = parseInt(totalMoney);
}
