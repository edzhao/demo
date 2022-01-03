-- ----------------------------
-- Procedure structure for cursorDateAdd
-- ----------------------------

DROP PROCEDURE IF EXISTS `cursorDateAdd`;
delimiter ;;
CREATE PROCEDURE `cursorDateAdd`()
BEGIN
	DECLARE id INT DEFAULT 0;
	DECLARE s INT DEFAULT 0;
	DECLARE val datetime DEFAULT now();
	DECLARE decode CURSOR FOR SELECT A.id as id FROM monitor_decode_data A WHERE A.time='2020-01-18 09:26:41.110';
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET s=1;
	OPEN decode;
	FETCH decode into id;
	WHILE s <> 1 DO
		UPDATE monitor_decode_data as B set B.time=val WHERE B.id=id;
		SET val = date_add(val,INTERVAL 1 SECOND);
		FETCH decode into id;
	END WHILE;
	CLOSE decode;
END;
delimiter ;;

-- ----------------------------
-- Procedure structure for genrateGamma
-- ----------------------------
DROP PROCEDURE IF EXISTS `genrateGamma`;
delimiter ;;
CREATE PROCEDURE `genrateGamma`()
BEGIN
	DECLARE id INT DEFAULT 0;
	DECLARE count INT DEFAULT 0;
	DECLARE data_word VARCHAR(100) DEFAULT 'GRBX,GRBUX,GRBDX';
	DECLARE valStr VARCHAR(10);
	DECLARE val INT DEFAULT 1;
	DECLARE dept INT DEFAULT 0;
	WHILE count < 3000 DO
		SET valStr = SUBSTRING_INDEX(SUBSTRING_INDEX(data_word,',',count%3+1),',',-1);
		IF count%3 = 0 THEN			
			SET dept = count;
		END IF;
		IF count%100 =0 THEN
			SET val = 1;
		END IF;
		INSERT INTO monitor_decode_data(location_area,raw_value,parity,start_time,end_time,location,MinN,time,data_word,`value`,dept) 
		value('云南九丝',val,'EVEN','2020/01/18 09:30:54.111','2020/01/18 09:31:20.110','[1, 4, 12, 17, 20]','5 : 26','2020-01-18 09:26:41.110',valStr,CONCAT(val*0.2197265625,' RPM'),dept);
		SET count = count+1;
		SET val = RAND()*1000;
	END WHILE;
END;
delimiter ;;

