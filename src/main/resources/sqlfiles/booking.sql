CREATE TABLE `roles` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `name` enum('ROLE_ADMIN','ROLE_MECHANIC','ROLE_USER') DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UKgdlljajjmqywje8kdxft3auoy` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE `user_roles` (
                              `user_id` bigint(20) NOT NULL,
                              `role_id` bigint(20) NOT NULL,
                              PRIMARY KEY (`user_id`,`role_id`),
                              KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
                              CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
                              CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `users` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `created_at` datetime(6) DEFAULT NULL,
                         `updated_at` datetime(6) DEFAULT NULL,
                         `access_token` varchar(255) DEFAULT NULL,
                         `address` varchar(255) DEFAULT NULL,
                         `email` varchar(255) DEFAULT NULL,
                         `image_url` varchar(255) DEFAULT NULL,
                         `is_active` bit(1) DEFAULT NULL,
                         `is_locked` bit(1) DEFAULT NULL,
                         `name` varchar(255) DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `phone` varchar(255) DEFAULT NULL,
                         `provider` enum('facebook','google','local') NOT NULL,
                         `provider_id` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
                         UNIQUE KEY `UKdu5v5sr43g5bfnji4vb8hg5s3` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `users` VALUES (1,'2024-08-18 04:26:13.615758','2024-08-18 08:21:14.251729','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzIzOTY5Mjc0LCJleHAiOjE3MjQ4MzMyNzR9.RzuIXLZfNukmFV__y-Cx_7CFVnAEyUDDevxHQgTTFwX84eqKZr_vDvA7AllMqzv4Sljp00Ah4H68DtwKdGUCXA',NULL,NULL,NULL,_binary '',_binary '\0',NULL,'$2a$10$bWSsQDlgySPjY8H46SIhA.5DKiDGor48foi2U1VoR8r091gGce.5G','0893726382','local',NULL),(2,'2024-08-18 04:28:57.030599','2024-08-18 09:03:01.549759','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNzIzOTcxNzgxLCJleHAiOjE3MjQ4MzU3ODF9.qcz9hGd6TY3D76KjQEztMu5aMWoTfVgsnT0vgm7PhEpgrajqE6VLVYYkGTcAFo5McCHA31VcbpuXQuafvGv-Ng',NULL,NULL,NULL,_binary '',_binary '\0',NULL,'$2a$10$xVG3zmquQShLBw5k/bBUOOTVvy99GiJZtCEHnMIQfYW8oPkbEKRKq','0893726322','local',NULL);

INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_MECHANIC'),(3,'ROLE_USER');

INSERT INTO `user_roles` VALUES (1,3),(2,3);
