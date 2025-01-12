package ru.itech.sno_api.service.implementation

import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import ru.itech.sno_api.dto.LectureDTO
import ru.itech.sno_api.entity.LectureEntity
import org.springframework.stereotype.Service
import ru.itech.sno_api.dto.ForumDTO
import ru.itech.sno_api.dto.SummaryDTO
import ru.itech.sno_api.entity.ForumEntity
import ru.itech.sno_api.entity.SummaryEntity
import ru.itech.sno_api.service.LectureService
import ru.itech.sno_api.repository.LectureRepository

@Service
class LectureServiceImplementation(
    private val lectureRepository: LectureRepository
): LectureService {

    override fun getAll(): List<LectureDTO> {
        return lectureRepository.findAll()
            .map { it.toDTO() }
    }

    override fun getById(lectureId: Long): LectureDTO {
        return lectureRepository.findById(lectureId)
            .orElseThrow { throw EntityNotFoundException("Lecture with ID $lectureId not found") }
            .toDTO()
    }

    override fun create(lecture: LectureDTO): LectureDTO {
        return lectureRepository.save(lecture.toEntity())
            .toDTO()
    }

    override fun update(lectureId: Long, lecture: LectureDTO): LectureDTO {
        val existingLecture = lectureRepository.findById(lectureId)
            .orElseThrow { throw EntityNotFoundException("Lecture with ID $lectureId not found") }

        existingLecture.lecturer = lecture.lecturer.toEntity()
        existingLecture.title = lecture.title
        existingLecture.description = lecture.description
        existingLecture.date = lecture.date
        existingLecture.summary = lecture.summary.toEntity()
        existingLecture.forum = lecture.forum.toEntity()

        return lectureRepository.save(existingLecture)
            .toDTO()
    }

    override fun delete(lectureId: Long) {
        lectureRepository.deleteById(lectureId)
    }


    override fun getAllPaginated(pageIndex: Int, pageSize: Int): List<LectureDTO> {
        return lectureRepository.findByOrderByLectureId(PageRequest.of(pageIndex, 2))
            .map { it.toDTO() }
    }
}

fun LectureEntity.toDTO(): LectureDTO {
    return LectureDTO(
        lectureId = lectureId,
        lecturer = lecturer.toDTO(),
        title = title,
        description = description,
        date = date,
        summary = summary.toDTO(),
        forum = forum.toDTO()
    )
}

fun LectureDTO.toEntity(): LectureEntity {
    return LectureEntity(
        lectureId = lectureId,
        lecturer = lecturer.toEntity(),
        title = title,
        description = description,
        date = date,
        summary = summary.toEntity(),
        forum = forum.toEntity()
    )
}



