create database sumodata;

drop table if exists c_induction_loop;
create table c_induction_loop(
	c_id	varchar(50) not null,
	c_lane	varchar(50) not null,
	c_position varchar(50) not null,
	primary key (c_id)
);

drop table if exists c_message_onloop;
create table c_message_onloop(
	c_id	bigint not null AUTO_INCREMENT,
	c_idstr	varchar(50) not null,
	c_vehicle_number int not null,
	c_mean_speed double not null,
	c_step_time int not null,
	primary key	(c_id),
	/*设置外键约束*/
	foreign key (c_idstr) references c_induction_loop(c_id)
);