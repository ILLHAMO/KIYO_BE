INSERT INTO store (store_id,bookmark_count,review_count,call_number,address,detail_comment,simple_comment,is_assigned,is_booked,is_kids,store_name,open_time)
values ( 1, 10,10,'02-4345-2234','성동구 왕십리','','',1,1,1,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 2, 30,10,'02-5253-8568','성동구 왕십리','','',1,0,1,'맘스터치','10:00 ~ 22:00'),
       ( 3, 2,10,'02-2665-3253','성동구 왕십리','','',1,0,1,'60계 치킨','10:00 ~ 22:00'),
       ( 4, 43,10,'02-2822-3793','성동구 왕십리','','',1,1,1,'지코바','10:00 ~ 22:00'),
       ( 5, 22,10,'02-4345-2234','성동구 왕십리','','',1,0,1,'맥도날드','10:00 ~ 22:00'),
       ( 6, 8,10,'02-4345-2234','성동구 왕십리','','',1,0,0,'버거킹','10:00 ~ 22:00'),
       ( 7, 56,10,'02-4345-2234','성동구 왕십리','','',1,0,0,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 8, 23,10,'02-4345-2234','성동구 왕십리','','',1,0,1,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 9, 58,10,'02-4345-2234','성동구 왕십리','','',1,1,0,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 10, 68,10,'02-4345-2234','성동구 왕십리','','',1,0,1,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 11, 92,10,'02-4345-2234','성동구 왕십리','','',1,1,1,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 12, 22,10,'02-4345-2234','성동구 왕십리','','',1,0,0,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 13, 10,10,'02-4345-2234','성동구 왕십리','','',1,0,0,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 14, 17,10,'02-4345-2234','성동구 왕십리','','',1,1,1,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 15, 48,10,'02-4345-2234','성동구 왕십리','','',1,0,1,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 16, 92,10,'02-4345-2234','성동구 왕십리','','',1,1,0,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 17, 73,10,'02-4345-2234','성동구 왕십리','','',1,0,1,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 18, 5,10,'02-4345-2234','성동구 왕십리','','',1,1,0,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 19, 74,10,'02-4345-2234','성동구 왕십리','','',1,0,1,'푸라닭 치킨','10:00 ~ 22:00'),
       ( 20, 98,10,'02-4345-2234','성동구 왕십리','','',1,0,1,'푸라닭 치킨','10:00 ~ 22:00');

INSERT INTO category (id,category_name)
values ( 1,'카페' ),
       ( 2,'한식' ),
       ( 3,'일식' ),
       ( 4,'중식' ),
       ( 5,'양식' ),
       ( 6,'아시안' ),
       ( 7,'비건' ),
       ( 8,'주점' );

INSERT INTO convenience (convenience_id,convenience_name)
values ( 1,'와이파이' ),
       ( 2,'단체석' ),
       ( 3,'애견동반' ),
       ( 4,'화장실' ),
       ( 5,'와이파이' ),
       ( 6,'단체석' ),
       ( 7,'애견동반' ),
       ( 8,'화장실' );

INSERT INTO store_category (store_id,category_ids)
values ( 1,1 ),
       ( 2,2 ),
       ( 3,3 ),
       ( 4,4 ),
       ( 5,5 ),
       ( 6,6 ),
       ( 7,7 ),
       ( 8,8 ),
       ( 9,1 ),
       ( 10,2 ),
       ( 11,3 ),
       ( 12,4 ),
       ( 13,5 ),
       ( 14,6 ),
       ( 15,7 ),
       ( 16,8 ),
       ( 17,1 ),
       ( 18,2 ),
       ( 19,3 ),
       ( 20,4 );

INSERT INTO store_convenience (store_id,convenience_ids)
values ( 1,1 ),
       ( 2,2 ),
       ( 3,3 ),
       ( 4,4 ),
       ( 5,5 ),
       ( 6,6 ),
       ( 7,7 ),
       ( 8,8 ),
       ( 9,1 ),
       ( 10,2 ),
       ( 11,3 ),
       ( 12,4 ),
       ( 13,5 ),
       ( 14,6 ),
       ( 15,7 ),
       ( 16,8 ),
       ( 17,1 ),
       ( 18,2 ),
       ( 19,3 ),
       ( 20,4 );

insert into menu (menu_id,menu_category,store_id)
values ( 1,"음료",1 ),
       ( 2,"음료",2 ),
       ( 3,"음료",3 ),
       ( 4,"음료",4 ),
       ( 5,"음료",5 ),
       ( 6,"음료",6 ),
       ( 7,"음료",7 ),
       ( 8,"음료",8 ),
       ( 9,"음료",9 ),
       ( 10,"음료",10 ),
       ( 11,"음료",11 ),
       ( 12,"음료",12 ),
       ( 13,"음료",13 ),
       ( 14,"음료",14 ),
       ( 15,"음료",15 ),
       ( 16,"음료",16 ),
       ( 17,"음료",17 ),
       ( 18,"음료",18 ),
       ( 19,"음료",19 ),
       ( 20,"음료",20 ),
       ( 21,"식사류",1 ),
       ( 22,"식사류",2 ),
       ( 23,"식사류",3 ),
       ( 24,"식사류",4 ),
       ( 25,"식사류",5 ),
       ( 26,"식사류",6 ),
       ( 27,"식사류",7 ),
       ( 28,"식사류",8 ),
       ( 29,"식사류",9 ),
       ( 30,"식사류",10 ),
       ( 31,"식사류",11 ),
       ( 32,"식사류",12 ),
       ( 33,"식사류",13 ),
       ( 34,"식사류",14 ),
       ( 35,"식사류",15 ),
       ( 36,"식사류",16 ),
       ( 37,"식사류",17 ),
       ( 38,"식사류",18 ),
       ( 39,"식사류",19 ),
       ( 40,"식사류",20 );

insert into menu_option (menu_option_id,menu_detail,menu_id)
values ( 1,"콜라",1 ),
       ( 2,"콜라",2 ),
       ( 3,"콜라",3 ),
       ( 4,"콜라",4 ),
       ( 5,"콜라",5 ),
       ( 6,"사이다",6 ),
       ( 7,"사이다",7 ),
       ( 8,"사이다",8 ),
       ( 9,"사이다",9 ),
       ( 10,"제로콜라",10 ),
       ( 11,"제로콜라",11 ),
       ( 12,"제로콜라",12 ),
       ( 13,"커피",13 ),
       ( 14,"커피",14 ),
       ( 15,"커피",15 ),
       ( 16,"커피",16 ),
       ( 17,"스프라이트",17 ),
       ( 18,"스프라이트",18 ),
       ( 19,"티",19 ),
       ( 20,"티",20 ),
       ( 21,"제육볶음",21 ),
       ( 22,"제육볶음",22 ),
       ( 23,"제육볶음",23 ),
       ( 24,"제육볶음",24 ),
       ( 25,"제육볶음",25 ),
       ( 26,"햄버거",26 ),
       ( 27,"햄버거",27 ),
       ( 28,"햄버거",28 ),
       ( 29,"햄버거",29 ),
       ( 30,"족발",30 ),
       ( 31,"족발",31 ),
       ( 32,"족발",32 ),
       ( 33,"족발",33 ),
       ( 34,"족발",34 ),
       ( 35,"보쌈",35 ),
       ( 36,"보쌈",36 ),
       ( 37,"보쌈",37 ),
       ( 38,"보쌈",38 ),
       ( 39,"보쌈",39 ),
       ( 40,"보쌈",40 );

insert into store_image (store_image_id,image_path,store_id)
values ( 1,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',1 ),
       ( 2,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',2 ),
       ( 3,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',3 ),
       ( 4,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',4 ),
       ( 5,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',5 ),
       ( 6,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',6 ),
       ( 7,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',7 ),
       ( 8,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',8 ),
       ( 9,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',9 ),
       ( 10,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',10 ),
       ( 11,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',11 ),
       ( 12,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',12 ),
       ( 13,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',13 ),
       ( 14,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',14 ),
       ( 15,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',15 ),
       ( 16,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',16 ),
       ( 17,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',17 ),
       ( 18,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',18 ),
       ( 19,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',19 ),
       ( 20,'https://kiyoimage.s3.ap-northeast-2.amazonaws.com/181b7593-e5b2-4acd-89e6-768422437f68.jpeg',20 );


