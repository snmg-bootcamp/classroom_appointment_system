***This is an early testing version of the api



#client side api:

##appointment arranging functions:
(the following functions should be used to arrange appointments)
sync (get everything)
get_current_appointments
add_new_appointments
remove_existing_appointments
get_classroom_list


##user auth funcitons:
(the following functions should be used to arrange user authentications)
user_login
user_logout
check_curent_session_validity




#server side api:




##shared:

structure_example:

    JSON 
    
    demo page: http://w181496.twbbs.org/curl_test/index.php

    A209 JSON structure example:
    
    [["","Sun 日(11.2)","Mon 一(11.3)","Tue 二(11.4)","Wed 三(11.5)","Thu 四(11.6)","Fri 五(11.7)","Sat 六(11.8)"],
    ["1 08:00-08:50","","","","","","陳日憲",""],
    ["2 09:00-09:50","","","","","","陳日憲",""],
    ["3 10:00-10:50","","","3A演算法實習課","線性代數-2A曾定章","影像處理曾定章","陳日憲",""],
    ["4 11:00-11:50","","","3A演算法實習課","線性代數-2A曾定章","影像處理曾定章","陳日憲",""],
    ["Z 12:00-12:50","","","","","","",""],
    ["5 13:00-13:50","","影像處理曾定章","","演算法-3A何錦文","演算法-3A何錦文","林鼎國",""],
    ["6 14:00-14:50","","線性代數-2A曾定章","機器學習栗永徽","","演算法-3A何錦文","林鼎國",""],
    ["7 15:00-15:50","","計算機網路-3B曾黎明","機器學習栗永徽","","離散數學-2B孫敏德","林鼎國",""],
    ["8 16:00-16:50","","計算機網路-3B曾黎明","機器學習栗永徽","","離散數學-2B孫敏德","林鼎國",""],
    ["9 17:00-17:50","","計算機網路-3B曾黎明","","劉于碩","離散數學-2B孫敏德","",""],
    ["A 18:00-18:50","","","","劉于碩","","",""],
    ["B 19:00-19:50","","","","","","",""],
    ["C 20:00-20:50","","","蘇俊儒","陳姿妤","","",""],
    ["D 21:00-21:50","","","蘇俊儒","陳姿妤","","",""]]

