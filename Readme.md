# Redis 연습 레포 1차
## 선착순 기프티콘 발행 연습

### TroubleShooting
> ‼️ 왜 문제가 생긴걸까?
- 맥북 자체에서 Redis를 활용하기 위한 로컬 환경에서의 Redis가 설치되어야함
```shell
$brew install redis
- homebrew로 서비스 시작
$brew services start redis

- 서비스 중단 
$brew services stop redis

- 서비스 재기동
$brew services restart redis

- redis 클라이언트로 접속
$redis-cli
```